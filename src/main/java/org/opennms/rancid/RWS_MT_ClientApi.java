package org.opennms.rancid;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class runs Rancid provisioning commands on a separated thread.
 * It is not a singleton so it can be instantiated more than once if more 
 * provisioning threads are needed.
 * Rule of thumb is the typical: if Rancid server has high latency then use
 * more threads (4 or 5) is Rancid server has very low latency use less threads (1 or 2).
 * The provisioning queue which stores the commands to be sent to Rancid is shared
 * between all the threads.
 *
 * The provisioning command will be called "message".
 * The message is passed by OpenNMS and is inserted into the buffer (mainBuffer).
 * The presence of a message in the buffer triggers the provisioner thread (or one of them
 * if more than one threads are active), the thread will execute the related command to Rancid.
 * 
 * If the provisioning fails due to a server busy error, the provisioner will
 * insert the message into another buffer (retry buffer) along with time information
 * and number of retries already done (the time information says when the message should
 * be reworked again).
 * A separate thread (this time a singleton) will wake up every 10 secs (by default)
 * and check the presence of a message into the retry buffer, if a message is found
 * and the timestamp is equal or earlier than the current time then it is inserted into the
 * main buffer to be re-worked by the provisioner, otherwise it is re-inserted
 * into the retry buffer.
 * If the number of retries exceed a given number, the message is logged and discarded.
 * 
 * @author <a href="mailto:guglielmoincisa@gmail.com">Guglielmo Incisa </a>
 * @author <a href="http://www.opennms.org/">OpenNMS </a>
 * 
 */

class Message {
    
   
    private RancidNode rn;
    private RancidNodeAuthentication rna;
    private int operation;
    private int retry;
    private long timestamp;
    public boolean doRancid;
    public boolean doAuth;
    
    ConnectionProperties connProp;
    
    public ConnectionProperties getConnectionProperties(){
        return connProp;
    }
    
    public RancidNode getRancidNode() {
        return rn;
    }
    public RancidNodeAuthentication getRancidNodeAuthentication() {
        return rna;
    }

    public int getOperation(){
        return operation;
    }
    public int getRetry() {
        return retry;
    }
    public long getTimestamp(){
        return timestamp;
    }
    public void setRanciNode(RancidNode rn){
        this.doRancid = true;;
        this.rn = rn;
    }
    public void setRancidNodeAuthentication(RancidNodeAuthentication rna){
        this.doAuth = true;
        this.rna = rna;
    }

    public void incRetry(){
        retry++;
    }
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }
    public Message(RancidNode rn, ConnectionProperties cp, int operation, int retry, int timestamp){
        this.doRancid = true;
        this.doAuth = false;
        this.rn = rn;
        this.connProp= cp;
        this.retry = retry;
        this.operation = operation;
        this.timestamp = timestamp;
    }
    public Message(RancidNode rn, RancidNodeAuthentication rna, ConnectionProperties cp, int operation, int retry, int timestamp){
        this.doRancid = true;
        this.doAuth = true;
        this.rn = rn;
        this.rna = rna;
        this.connProp= cp;
        this.retry = retry;
        this.operation = operation;
        this.timestamp = timestamp;
    }
    public Message(int token){
        //reserved
        // the token is used in the retry buffer and says if
        // the queue has been inspected entirely
        this.operation = token;
    }

}

/*
 * This is a singleton, wakes up every 10 secs (configurable) and check
 * the queue for the presence of provisioning commands
 */
class RetryThread extends Thread {
    
	private static ConcurrentLinkedQueue<Message> retryBuffer = new ConcurrentLinkedQueue<Message>();
    
    private int sleepTime = 10000;
    
    private static volatile RetryThread instance;
    
    private RetryThread() {}
    
    public static RetryThread getInstance(){
        if (instance == null) {
            instance = new RetryThread();
        }
        return instance;
    }
       
    public void setSleepTime(final int sleepTime){
        this.sleepTime = sleepTime;
    }
    
    public void init(){
        System.out.println("RetryThread.init()");
        putMessage(new Message(RWS_MT_ClientApi.TOKEN));
    }

    /**
     * @deprecated passing a class that's only called statically
     * @param mt not used
     */
    public void init(final RWS_MT_ClientApi mt){
    	init();
    }
    
    // Check the queue if it finds the token goes to sleep
    // else check the time of the message
    // if time has elapsed the message is processed otherwise it
    // will be put in retry queue again
    public void run(){
        System.out.println("RetryThread.run() called");
        while(true){
            System.out.println("RetryThread.run() loop");
    
            Message x = retryBuffer.poll();
            if (x.getOperation() == RWS_MT_ClientApi.TOKEN){
                
                retryBuffer.add(x);

                System.out.println("RetryThread.run() token found");
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }
            else {
                System.out.println("RetryThread.run() message found " +x.getRancidNode().getDeviceName());
                long i = System.currentTimeMillis();
                System.out.println("RetryThread.run() message found timestamp " + x.getTimestamp() + " current " + i);
                if (x.getTimestamp() <= i){
                    System.out.println("RetryThread.run() message rescheduled");
                    RWS_MT_ClientApi.reDoWork(x);
                }
                else {
                    System.out.println("RetryThread.run() message delayed");
                    retryBuffer.add(x);
                }
            }
        }
    }
    
    public static void putMessage(Message m){
        retryBuffer.add(m);
    }
}

// Threaded Provisioner
public class RWS_MT_ClientApi extends Thread {
    
    public static int ADD_NODE =    1;
    public static int UPDATE_NODE = 2;
    public static int DELETE_NODE = 3;
    
    public static int UP_NODE = 4;
    public static int DOWN_NODE = 5;
    
    public static int TOKEN = 100; 
    
    private long retryDelay = 30000;
    private int maxRetry = 3;
    
    private static boolean inited = false; 
    
    private static ConcurrentLinkedQueue<Message> mainBuffer = new ConcurrentLinkedQueue<Message>();
    

    
    // These are needed to put on hold the main thread if there
    // are no messages to be processed
    final private static Lock lock = new ReentrantLock();
    final private static Condition hasMessage = lock.newCondition(); 

     
    public void init() {
        if(inited)
            return;
        System.out.println("RWS_MT_ClientApi.init() called");
        RWSClientApi.init();        
        inited = true;
        
        RetryThread.getInstance().init();
        RetryThread.getInstance().start();
    }
    
    public void setRetryDelaySeconds(int seconds){
        retryDelay = seconds*1000;
    }
    
    public void setMaxRetry(int maxRetry){
        this.maxRetry = maxRetry;
    }
    
    // Main thread
    public void run(){
        System.out.println("RWS_MT_ClientApi.run() called");
        while(true){
            try {

                lock.lock();
                try {
                    while (mainBuffer.isEmpty()){
                        System.out.println("RWS_MT_ClientApi.run() await");
                        hasMessage.await();
                    }
                    System.out.println("RWS_MT_ClientApi.run() rancidIt");
                    Message x = mainBuffer.poll(); 
                    rancidIt(x);
                } 
                finally {
                    lock.unlock();
                }
            }
            catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
            catch (RancidApiException e){
                System.out.println(e.getMessage());
            }
        }
    }
    

    protected static void reDoWork(Message m){
        System.out.println("RWS_MT_ClientApi.reDoWork() called");
        
        lock.lock();
        
        try {
            mainBuffer.add(m);
            hasMessage.signal();
        } finally {
            lock.unlock();
        }
    }
    
    // Get provisioning command and inserts the related message
    // into the buffer
    // trigger the condition variable to execute the thread
    private void doWork(Message m) throws InterruptedException {
        System.out.println("RWS_MT_ClientApi.doWork() called");
       
        lock.lock();
        
        try {
            mainBuffer.add(m);
            hasMessage.signal();
        } finally {
            lock.unlock();
        }
    }

    // Execute the RWS command
    // If the server is busy, retry a given number of times
    // if it still fails then throws the exception 
    private void rancidIt(Message m) throws RancidApiException{
        System.out.println("RWS_MT_ClientApi.rancidIt() called");
        try {
            if (m.getOperation() == ADD_NODE) {
                System.out.println("RWS_MT_ClientApi.rancidIt() ADD_NODE " + m.getRancidNode().getDeviceName());
        
                if (m.doRancid){
                    RWSClientApi.createRWSRancidNode(m.getConnectionProperties(), m.getRancidNode());
                    m.doRancid = false;
                }
                if (m.doAuth){
                    RWSClientApi.createOrUpdateRWSAuthNode(m.getConnectionProperties(), m.getRancidNodeAuthentication());
                    m.doAuth = false;
                }
            }    
            else if (m.getOperation() == UPDATE_NODE) {
                System.out.println("RWS_MT_ClientApi.rancidIt() UPDATE_NODE" + m.getRancidNode().getDeviceName());
                
                if (m.doRancid){
                    RWSClientApi.updateRWSRancidNode(m.getConnectionProperties(), m.getRancidNode());   
                    m.doRancid = false;
                }
                if (m.doAuth){
                    RWSClientApi.createOrUpdateRWSAuthNode(m.getConnectionProperties(), m.getRancidNodeAuthentication());
                    m.doAuth = false;
                }
            }
            else if (m.getOperation() == DELETE_NODE) {
                System.out.println("RWS_MT_ClientApi.rancidIt() DELETE_NODE" + m.getRancidNode().getDeviceName());
         
                if (m.doRancid){
                    RWSClientApi.deleteRWSRancidNode(m.getConnectionProperties(), m.getRancidNode());   
                    m.doRancid = false;
                }
                if (m.doAuth){
                    RWSClientApi.deleteRWSAuthNode(m.getConnectionProperties(), m.getRancidNodeAuthentication());
                    m.doAuth = false;
                }
            }
        }
        catch (RancidApiException e) {
            if (e.getRancidCode() == RancidApiException.RWS_BUSY) {
                System.out.println("RWS_MT_ClientApi.rancidIt got exception");
                if (m.getRetry() >= maxRetry) {
                    throw(new RancidApiException("Error: Server Busy", RancidApiException.RWS_BUSY));
                }
                m.incRetry();
                long i = System.currentTimeMillis()+ retryDelay;
                System.out.println("RWS_MT_ClientApi.rancidIt inserting into retry buffer " + i);
                m.setTimestamp(i);
                RetryThread.putMessage(m);
            }
        }
    }

    //Public methods
    public void addNode(RancidNode rn, ConnectionProperties cp) throws RancidApiException, InterruptedException{
        System.out.println("RWS_MT_ClientApi.addNode() called");
        Message m = new Message (rn, cp, ADD_NODE, 0, 0);
        doWork(m);
    }
    public void updNode(RancidNode rn, ConnectionProperties cp) throws RancidApiException, InterruptedException{
        Message m = new Message (rn, cp, UPDATE_NODE, 0, 0);
        System.out.println("RWS_MT_ClientApi.updNode() called");
        doWork(m);
    }
    public void delNode(RancidNode rn, ConnectionProperties cp) throws RancidApiException, InterruptedException{
        Message m = new Message (rn, cp, DELETE_NODE, 0, 0);
        System.out.println("RWS_MT_ClientApi.delNode() called");
        doWork(m);
    }
    public void addNode(RancidNode rn, RancidNodeAuthentication rna, ConnectionProperties cp) throws RancidApiException, InterruptedException{
        Message m = new Message (rn, rna, cp, ADD_NODE, 0, 0);
        System.out.println("RWS_MT_ClientApi.addNode() called");
        doWork(m);
    }
    public void updNode(RancidNode rn, RancidNodeAuthentication rna, ConnectionProperties cp) throws RancidApiException, InterruptedException{
        Message m = new Message (rn, rna, cp, UPDATE_NODE, 0, 0);
        System.out.println("RWS_MT_ClientApi.updNode() called");
        doWork(m);
    }
    public void delNode(RancidNode rn, RancidNodeAuthentication rna, ConnectionProperties cp) throws RancidApiException, InterruptedException{
        Message m = new Message (rn, rna, cp, DELETE_NODE, 0, 0);
        System.out.println("RWS_MT_ClientApi.delNode() called");
        doWork(m);
    }
}
