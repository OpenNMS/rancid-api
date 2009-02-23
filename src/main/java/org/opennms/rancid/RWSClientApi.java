package org.opennms.rancid;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;


import java.io.IOException;

import org.restlet.Client;
//import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.data.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.ChallengeResponse;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;

/**
 * This class is the main client for the Rancid API.
 * 
 * 
 * @author <a href="mailto:guglielmoincisa@gmail.com">Guglielmo Incisa </a>
 * @author <a href="http://www.opennms.org/">OpenNMS </a>
 * 
 */

class RWSResourceListImpl implements RWSResourceList {
    
    public RWSResourceListImpl(){
    }
    
    public List<String> ResourcesList;    
    
    public String getResource(int i) {
        //if ....TODO
        return ResourcesList.get(i);
    }
    public List<String> getResource() {
        //if ....TODO
        return ResourcesList;
    }    
}

public class RWSClientApi {
   
    private static Client client=new Client(Protocol.HTTP);
    
    private static boolean inited=false;
 
    public static void init(){

        try {
            client.start();
            inited = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    //Test encoding againts test server http://www.rionero.com/cgi-bin/cgitest
    public static void encode_test() throws RancidApiException {
        
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        Form form = new Form();
        form.add("deviceType", "test 123");
        form.add("state", "rotto al 99%");
        form.add("comment", "test commento");
                
        Representation rep = form.getWebRepresentation();

        //rep.setMediaType(MediaType.APPLICATION_XHTML_XML);
        // Launch the request
        Reference rwsTest= new Reference("http://www.rionero.com/cgi-bin/cgitest");
        Response response = client.post(rwsTest,rep);
        try {
            response.getEntity().write(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
      
    //***************************************************************************
    //***************************************************************************
    //LISTS    

    // Generic list of resources
    public static RWSResourceList getRWSResourceList(ConnectionProperties cp, String subUri ) throws RancidApiException {

        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp, subUri);
        return rwsImpl;
    }
    public static RWSResourceList getRWSResourceList(String baseUri, String subUri ) throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"",30);
        return getRWSResourceList(cp, subUri);
    }
    
    //***************************************************************************
    // Services list
    public static RWSResourceList getRWSResourceServicesList(ConnectionProperties cp) throws RancidApiException {

        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp , "/");
        return rwsImpl;
    }
    public static RWSResourceList getRWSResourceServicesList(String baseUri ) throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSResourceServicesList(cp);
    }

    //***************************************************************************
    // RancidApi resource list
    public static RWSResourceList getRWSResourceRAList(ConnectionProperties cp) throws RancidApiException {

        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp, "/rancid/");
        return rwsImpl;
    }
    public static RWSResourceList getRWSResourceRAList(String baseUri) throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSResourceRAList(cp);
    }
    //***************************************************************************
    // Group list
    public static RWSResourceList getRWSResourceGroupsList(ConnectionProperties cp) throws RancidApiException {

        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp,"/rancid/groups/");
        return rwsImpl;
    }
    public static RWSResourceList getRWSResourceGroupsList(String baseUri) throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSResourceGroupsList(cp);
    }
    
    //***************************************************************************
    // Device list
    public static RWSResourceList getRWSResourceDeviceList(ConnectionProperties cp, String group) throws RancidApiException {
        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp,"/rancid/groups/"+group+"/");
        return rwsImpl;
    }
    public static RWSResourceList getRWSResourceDeviceList(String baseUri, String group) throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSResourceDeviceList(cp, group);
    }

    //***************************************************************************
    // Login Pattern List
    public static RWSResourceList getRWSResourceLoginPatternList(ConnectionProperties cp)  throws RancidApiException {
        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp, "/rancid/clogin/");
        return rwsImpl;
    }
    public static RWSResourceList getRWSResourceLoginPatternList(String baseUri)  throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSResourceLoginPatternList(cp);
    }
    //***************************************************************************
    // Version List
    public static RWSResourceList getRWSResourceConfigList(ConnectionProperties cp, String group, String deviceName)  throws RancidApiException {
        RWSResourceListImpl rwsImpl = new RWSResourceListImpl();
        rwsImpl.ResourcesList = getInfo(cp, "/rancid/groups/"+group+"/"+deviceName+"/configs/");
        return rwsImpl;
    }
     
    public static RWSResourceList getRWSResourceConfigList(String baseUri, String group, String deviceName)  throws RancidApiException {
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSResourceConfigList(cp, group, deviceName);
    }
     
    //***************************************************************************
    //***************************************************************************
    // getInfo
    // gets generic list of items
    private static List<String> getInfo(ConnectionProperties cp, String listUri) throws RancidApiException{

        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }

        Response response=getMethodRWS(cp, cp.getUrl()+cp.getDirectory()+listUri);
        DomRepresentation dmr = response.getEntityAsDom();
        
        List<String> data = new ArrayList<String>();
        
        try {
            Document doc = dmr.getDocument();
            //dmr.write(System.out);
            for (int ii = 0; ii < doc.getElementsByTagName("Resource").getLength() ; ii++) {
                String tmp = doc.getElementsByTagName("Resource").item(ii).getTextContent();
                //System.out.println("Element " + tmp);
                data.add(tmp);
            }
        }
        catch( IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
    
    //***************************************************************************
    //***************************************************************************
    //Rancid Node Info retrieve
    
    public static RancidNode getRWSRancidNode(ConnectionProperties cp ,String group, String devicename) throws RancidApiException{

        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        Response response =getMethodRWS(cp, cp.getUrl() + cp.getDirectory()+"/rancid/groups/" + group + "/" + devicename);
        DomRepresentation dmr = response.getEntityAsDom();
        
        RancidNode rn = new RancidNode();
       
        try {
            Document doc = dmr.getDocument();

            rn.setDeviceName(devicename);
            rn.setDeviceType(doc.getElementsByTagName("deviceType").item(0).getTextContent());
            rn.setStateUp(doc.getElementsByTagName("state").item(0).getTextContent().compareTo("up") == 0);
            rn.setComment(doc.getElementsByTagName("comment").item(0).getTextContent());
            rn.setGroup(group);
            
        }
        catch( IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //http://www.rionero.com/rws-current/rws/rancid/groups/laboratorio/7206PED.wind.lab/configs
        //Get the following
//        <TotalRevisions>18</TotalRevisions>
//        <HeadRevision>1.18</HeadRevision>
//        <UrlViewVC>
//        http://www.rionero.com/viewvc/laboratorio/configs/7206PED.wind.lab?view=log
//        </UrlViewVC>
        Response response2 = getMethodRWS(cp, cp.getUrl() + cp.getDirectory()+"/rancid/groups/" + group + "/" + devicename+"/configs");
        DomRepresentation dmr2 = response2.getEntityAsDom();
   
        try {
            Document doc = dmr2.getDocument();

            rn.setRootConfigurationUrl(doc.getElementsByTagName("UrlViewVC").item(0).getTextContent());
            rn.setTotalRevisions(doc.getElementsByTagName("TotalRevisions").item(0).getTextContent());
            rn.setHeadRevision(doc.getElementsByTagName("HeadRevision").item(0).getTextContent());        
        }
        catch( IOException e){
            // TODO Auto-generated catch block
            //Silent
        }
        return rn;
    }
        
    public static RancidNode getRWSRancidNode(String baseUri ,String group, String devicename) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSRancidNode(cp, group, devicename );
    }
    
//    public static RancidNodeAggregate getRancidNodeAggregate(String baseUri,String deviceName) throws RancidApiException {
//        
//        if (!inited){
//            throw(new RancidApiException("Error: Api not initialized"));
//        }
//        
//        try {
//            RWSResourceList groups = getRWSResourceGroupsList(baseUri);
//
//    
//            List<String> groupList = groups.getResource();
//            
//            Iterator iter = groupList.iterator();
//            
//            Object tmpGroup;
//            
//            RancidNodeAggregate rna = new RancidNodeAggregate();
//            
//            rna.setGroups(groupList);
//            
//            while (iter.hasNext()) {
//                RancidNode rn = new RancidNode();
//                tmpGroup = iter.next();
//                try {
//                    //System.out.println("Adding " + (String)tmpGroup + " " + deviceName);
//                    rn = getRWSRancidNodeInventory(baseUri ,(String)tmpGroup, deviceName);
//                    rna.addRancidAggregate((String)tmpGroup, rn);
//                }
//                catch (Exception e) {
//                    //skip, Rancid Node not in group
//                    //but at least in one group
//                }
//            }
//            return rna;
//        }
//        catch (Exception e){
//                e.printStackTrace();
//        }
//        return null;
//    }
    
    //***************************************************************************
    //Rancid Group provisioning
    // incomplete
    //TODO
    
//    public static void createRWSGroup(String baseUri, String group) throws RancidApiException{
//        
//        if (!inited){
//            throw(new RancidApiException("Error: Api not initialized"));
//        }
//        
//        Form form = new Form();
//        form.add("Resource", group );
//                
//        Representation rep = form.getWebRepresentation();
//
//        Response response = putMethodRWS(cp, baseUri+"/rws/rancid/groups/" + group,rep);
//        try {
//            response.getEntity().write(System.out);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
    
    //***************************************************************************
    //***************************************************************************
    //Rancid Node Info provisioning
    
    public static void create_TEST_RWSRancidNode(String baseUri, RancidNode rnode) throws RancidApiException{
        throw(new RancidApiException("Error: Server Busy", RancidApiException.RWS_BUSY));
    }
    
    public static void createRWSRancidNode(ConnectionProperties cp, RancidNode rnode) throws RancidApiException{
        
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        Form form = new Form();
        form.add("deviceType", rnode.getDeviceType());
        form.add("state", rnode.getState());
        form.add("comment", rnode.getComment());
                
        Representation rep = form.getWebRepresentation();

        Response response = putMethodRWS(cp, cp.getUrl()+cp.getDirectory()+"/rancid/groups/"+rnode.getGroup()+"/"+rnode.getDeviceName(),rep);
        
        DomRepresentation dmr2 = response.getEntityAsDom();
        
        try {
            Document doc = dmr2.getDocument();

            if (doc.getElementsByTagName("Code").item(0).getTextContent().compareTo("ErrBusy") == 0){
                throw(new RancidApiException("Error: Server Busy", RancidApiException.RWS_BUSY));
            }        
            //response.getEntity().write(System.out);
        } catch (IOException e) {
            throw(new RancidApiException("Error: IOException", RancidApiException.OTHER_ERROR));
        }
    }
    public static void createRWSRancidNode(String baseUri, RancidNode rnode) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        createRWSRancidNode(cp, rnode);
    }
    
    //***************************************************************************
    
    public static void updateRWSRancidNode(ConnectionProperties cp, RancidNode rnode) throws RancidApiException{

        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        Form form = new Form();
        form.add("deviceType", rnode.getDeviceType());
        form.add("state", rnode.getState());
        form.add("comment", rnode.getComment());
                
        Representation rep = form.getWebRepresentation();

        System.out.println(cp.getUrl()+cp.getDirectory()+"/rancid/groups/"+rnode.getGroup()+"/"+rnode.getDeviceName());
        Response response = postMethodRWS(cp, cp.getUrl()+cp.getDirectory()+"/rancid/groups/"+rnode.getGroup()+"/"+rnode.getDeviceName(),rep);
        
        DomRepresentation dmr2 = response.getEntityAsDom();
        
        try {
            Document doc = dmr2.getDocument();

            if (doc.getElementsByTagName("Code").item(0).getTextContent().compareTo("ErrBusy") == 0){
                throw(new RancidApiException("Error: Server Busy", RancidApiException.RWS_BUSY));
            }        
        } catch (IOException e) {
            throw(new RancidApiException("Error: IOException", RancidApiException.OTHER_ERROR));
        }
    }
    public static void updateRWSRancidNode(String baseUri, RancidNode rnode) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        updateRWSRancidNode(cp, rnode);
    }

    //***************************************************************************
    public static void deleteRWSRancidNode(ConnectionProperties cp, RancidNode rnode) throws RancidApiException{
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        Form form = new Form();
        form.add("deviceType", rnode.getDeviceType());
        form.add("state", rnode.getState());
        form.add("comment", rnode.getComment());
                
//        Representation rep = form.getWebRepresentation();

        //rep.setMediaType(MediaType.APPLICATION_XHTML_XML);
        // Launch the request
        System.out.println(cp.getUrl()+cp.getDirectory()+"/rancid/groups/"+rnode.getGroup()+"/"+rnode.getDeviceName());

        Response response = deleteMethodRWS(cp, cp.getUrl()+cp.getDirectory()+"/rancid/groups/"+rnode.getGroup()+"/"+rnode.getDeviceName());
        
        DomRepresentation dmr2 = response.getEntityAsDom();
        
        try {
            Document doc = dmr2.getDocument();

            if (doc.getElementsByTagName("Code").item(0).getTextContent().compareTo("ErrBusy") == 0){
                throw(new RancidApiException("Error: Server Busy", RancidApiException.RWS_BUSY));
            }        
        } catch (IOException e) {
            throw(new RancidApiException("Error: IOException", RancidApiException.OTHER_ERROR));
        }
    }
    public static void deleteRWSRancidNode(String baseUri, RancidNode rnode) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        deleteRWSRancidNode(cp, rnode);
    }
    
    //***************************************************************************



    //***************************************************************************
    //***************************************************************************
    //Inventory Node info retrieve 
    public static InventoryNode getRWSInventoryNode(ConnectionProperties cp, RancidNode rancidNode, String version) throws RancidApiException{
    
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        //System.out.println(baseUri + "/rws/rancid/groups/" + rancidNode.getGroup() + "/" + rancidNode.getDeviceName()+"/configs/"+version);
        Response response = getMethodRWS(cp, cp.getUrl() + cp.getDirectory()+"/rancid/groups/" + rancidNode.getGroup() + "/" + rancidNode.getDeviceName()+"/configs/"+version );
        DomRepresentation dmr = response.getEntityAsDom();
        
        InventoryNode in = new InventoryNode(rancidNode);
               
        //TODO get inventory too
        
        try {
            Document doc = dmr.getDocument();
            // 2008/11/13 13:54:35 UTC
            SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:m:s z");
            //System.out.println("DATA "+doc.getElementsByTagName("Date").item(0).getTextContent());
            Date date = format.parse(doc.getElementsByTagName("Date").item(0).getTextContent());
            in.setCreationDate(date);
            in.setConfigurationUrl(doc.getElementsByTagName("UrlViewVC").item(0).getTextContent());
            
            in.setVersionId(version);
                      
        }
        catch( IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return in;

    }
    public static InventoryNode getRWSInventoryNode(RancidNode rancidNode, String baseUri,  String version ) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSInventoryNode(cp , rancidNode, version);
    }

    //***************************************************************************
    //***************************************************************************
    //Inventory Node info retrieve 
    public static RancidNode getRWSRancidNodeInventory(ConnectionProperties cp, String group, String deviceName) throws RancidApiException{
        
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        RWSResourceList versions = getRWSResourceConfigList(cp.getUrl(), group, deviceName);
        RancidNode rn = getRWSRancidNode(cp.getUrl(), group, deviceName);
        
        List<String> configlist = versions.getResource();
        Iterator iter1 = configlist.iterator();
        String tmpg1;
        
        while (iter1.hasNext()) {
            tmpg1 = (String)iter1.next();
            //System.out.println("Version " + tmpg1);
            InventoryNode in = getRWSInventoryNode(rn, cp.getUrl(), tmpg1);
            rn.addInventoryNode(tmpg1, in);
        }
        
        return rn;

    }

    public static RancidNode getRWSRancidNodeInventory(String baseUri, String group, String deviceName) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSRancidNodeInventory(cp, group, deviceName);
    }

    
    //***************************************************************************
    //***************************************************************************
    //Authentication info retrieve
    public static RancidNodeAuthentication getRWSAuthNode(ConnectionProperties cp, String devicename) throws RancidApiException{
        
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        Response response = getMethodRWS(cp, cp.getUrl() + cp.getDirectory()+"/rancid/clogin/" + devicename);
        DomRepresentation dmr = response.getEntityAsDom();
        
        RancidNodeAuthentication rna = new RancidNodeAuthentication();
               
        try {
            Document doc = dmr.getDocument();
            // dmr.write(System.out);
            rna.setDeviceName(devicename);
            rna.setUser(doc.getElementsByTagName("user").item(0).getTextContent());
            rna.setPassword(doc.getElementsByTagName("password").item(0).getTextContent());
            rna.setEnablePass(doc.getElementsByTagName("enablepassword").item(0).getTextContent());
            rna.setConnectionMethod(doc.getElementsByTagName("method").item(0).getTextContent());
            try {
                rna.setAutoEnable(doc.getElementsByTagName("autoenable").item(0).getTextContent().compareTo("1") == 0);
            }
            catch (Exception e) {
                System.out.println("auto enable field not found");
            }
            //rna.setAuthType(doc.getElementsByTagName("authType").item(0).getTextContent());
            //System.out.println("nel metodo "+ doc.getElementsByTagName("method").item(0).getTextContent());


        }
        catch( IOException e){
            // TODO Auto-generated catch block
            System.out.println("IOException " + e.getMessage());
            e.printStackTrace();
        }
        //TODO get data
        // copy data
        return rna;

    }
    public static RancidNodeAuthentication getRWSAuthNode(String baseUri, String devicename) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        return getRWSAuthNode(cp, devicename);
    }
    
    
    
    //***************************************************************************
    //***************************************************************************
    //RancidNodeAuthetication provisioning
    public static void createOrUpdateRWSAuthNode(ConnectionProperties cp, RancidNodeAuthentication rnodea) throws RancidApiException{
        
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        
        System.out.println("createOrUpdateRWSAuthNode changing " + cp.getUrl() + "Node " + rnodea.getDeviceName());
        
        Form form = new Form();
        form.add("user", rnodea.getUser());
        form.add("password", rnodea.getPassword());
        form.add("enablepassword", rnodea.getEnablePass());
        String autoenable = "0";
        if (rnodea.isAutoEnable()){
            autoenable="1";
        }
        form.add("autoenable", autoenable);
//        form.add("authType", rnodea.getAuthType());
        form.add("method", rnodea.getConnectionMethodString());
        
        Representation rep = form.getWebRepresentation();

        // Launch the request
        
        Reference rwsTest= new Reference(cp.getUrl() + cp.getDirectory() + "/rancid/clogin/" +rnodea.getDeviceName());
        Response response = postMethodRWS(cp, cp.getUrl() + cp.getDirectory() + "/rancid/clogin/" +rnodea.getDeviceName(), rep);
        try {
            System.out.println("createOrUpdateRWSAuthNode sent:");
            response.getEntity().write(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("createOrUpdateRWSAuthNode exception " + e.getMessage());
            e.printStackTrace();
        }

    }
    public static void createOrUpdateRWSAuthNode(String baseUri, RancidNodeAuthentication rnodea) throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        createOrUpdateRWSAuthNode(cp, rnodea);
        return;

    }   
    
    public static void deleteRWSAuthNode(ConnectionProperties cp, RancidNodeAuthentication rnodea)throws RancidApiException{
        if (!inited){
            throw(new RancidApiException("Error: Api not initialized"));
        }
        Form form = new Form();
        form.add("user", rnodea.getUser());
        form.add("password", rnodea.getPassword());
        form.add("enablepassword", rnodea.getEnablePass());
//        form.add("autoEnable", rnodea.isAutoEnable());
//        form.add("authType", rnodea.getAuthType());
        form.add("method", rnodea.getConnectionMethodString());
//        form.add("user", rnodea.getUser());
        
//        Representation rep = form.getWebRepresentation();

        //rep.setMediaType(MediaType.APPLICATION_XHTML_XML);
        // Launch the request
        System.out.println(cp.getUrl() + cp.getDirectory()+"/rancid/clogin/" +rnodea.getDeviceName());
        Response response = deleteMethodRWS(cp, cp.getUrl() + cp.getDirectory() + "/rancid/clogin/" +rnodea.getDeviceName());
        try {
            response.getEntity().write(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }

    
    
    public static void deleteRWSAuthNode(String baseUri, RancidNodeAuthentication rnodea)throws RancidApiException{
        ConnectionProperties cp = new ConnectionProperties("","",baseUri,"/rws",30);
        deleteRWSAuthNode(cp, rnodea);
        return;
    }
    
    //*********************************************************************************************
    // RWS METHODS
    static Response getMethodRWS(ConnectionProperties cp, String uriReference) throws RancidApiException {
        
        client.setConnectTimeout(cp.getTimeout());
        
        Request request = new Request(Method.GET, uriReference);
        
        if(cp.getUserName() != null){
            
            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,cp.getUserName(), cp.getPassword());
            

            request.setChallengeResponse(authentication);
        }
        else {
            
        }
        Response response = client.handle(request); 
        
        if (response.getStatus().isSuccess()) {
            return response;
        } else if (response.getStatus() == Status.CLIENT_ERROR_REQUEST_TIMEOUT){
            throw(new RancidApiException("Error: RWS GET request timeout "));
        }else if (response.getStatus() == Status.CLIENT_ERROR_UNAUTHORIZED){
            throw(new RancidApiException("Error: RWS GET authentication failed"));
        } else {
            throw(new RancidApiException("Error: RWS GET request failed: "+ response.getStatus()));
        }
        
    }
    static Response postMethodRWS(ConnectionProperties cp, String uriReference, Representation form) throws RancidApiException {
        
        client.setConnectTimeout(cp.getTimeout());
        
        Request request = new Request(Method.POST, uriReference, form);
        
        if(cp.getAuthOn()){
            
            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,cp.getUserName(), cp.getPassword());
            

            request.setChallengeResponse(authentication);
        }
        else {
            
        }
        Response response = client.handle(request); 
        
        if (response.getStatus().isSuccess()) {
            return response;
        } else if (response.getStatus() == Status.CLIENT_ERROR_REQUEST_TIMEOUT){
            throw(new RancidApiException("Error: RWS POST request timeout "));
        }else if (response.getStatus() == Status.CLIENT_ERROR_UNAUTHORIZED){
            throw(new RancidApiException("Error: RWS POST authentication failed"));
        } else {
            throw(new RancidApiException("Error: RWS POST request failed: "+ response.getStatus()));
        }
        
    }
    static Response putMethodRWS(ConnectionProperties cp, String uriReference, Representation form) throws RancidApiException {
        
        client.setConnectTimeout(cp.getTimeout());
        
        Request request = new Request(Method.PUT, uriReference, form);
        
        if(cp.getAuthOn()){
            
            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,cp.getUserName(), cp.getPassword());
            

            request.setChallengeResponse(authentication);
        }
        else {
            
        }
        Response response = client.handle(request); 
        
        if (response.getStatus().isSuccess()) {
            return response;
        } else if (response.getStatus() == Status.CLIENT_ERROR_REQUEST_TIMEOUT){
            throw(new RancidApiException("Error: RWS PUT request timeout "));
        }else if (response.getStatus() == Status.CLIENT_ERROR_UNAUTHORIZED){
            throw(new RancidApiException("Error: RWS PUT authentication failed"));
        } else {
            throw(new RancidApiException("Error: RWS PUT request failed: "+ response.getStatus()));
        }
        
    }
    static Response deleteMethodRWS(ConnectionProperties cp, String uriReference) throws RancidApiException {
        
        client.setConnectTimeout(cp.getTimeout());
        
        Request request = new Request(Method.DELETE, uriReference);
        
        if(cp.getAuthOn()){
            
            ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
            ChallengeResponse authentication = new ChallengeResponse(scheme,cp.getUserName(), cp.getPassword());
            

            request.setChallengeResponse(authentication);
        }
        else {
            
        }
        Response response = client.handle(request); 
        
        if (response.getStatus().isSuccess()) {
            return response;
        } else if (response.getStatus() == Status.CLIENT_ERROR_REQUEST_TIMEOUT){
            throw(new RancidApiException("Error: RWS DELETE request timeout "));
        }else if (response.getStatus() == Status.CLIENT_ERROR_UNAUTHORIZED){
            throw(new RancidApiException("Error: RWS DELETE authentication failed"));
        } else {
            throw(new RancidApiException("Error: RWS DELETE request failed: "+ response.getStatus()));
        }
        
    }
    
    //private  String BaseUri;
    
//user password
    /*
    # // Prepare the request  
    # Request request = new Request(Method.GET, "http://localhost:8182/");  
    #   
    # // Add the client authentication to the call  
    # ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;  
    # ChallengeResponse authentication = new ChallengeResponse(scheme,  
    #         "scott", "tiger");  
    # request.setChallengeResponse(authentication);  
    #   
    # // Ask to the HTTP client connector to handle the call  
    # Client client = new Client(Protocol.HTTP);  
    # Response response = client.handle(request);  
    #   
    # if (response.getStatus().isSuccess()) {  
    #     // Output the response entity on the JVM console  
    #     response.getEntity().write(System.out);  
    # } else if (response.getStatus()  
    #         .equals(Status.CLIENT_ERROR_UNAUTHORIZED)) {  
    #     // Unauthorized access  
    #     System.out  
    #             .println("Access authorized by the server, " +  
    #                     "check your credentials");  
    # } else {  
    #     // Unexpected status  
    #     System.out.println("An unexpected status was returned: "  
    #             + response.getStatus());  
    # } 
    */ 
  
 
}



    