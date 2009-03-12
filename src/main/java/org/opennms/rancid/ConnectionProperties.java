package org.opennms.rancid;

public class ConnectionProperties {
    
    private String userName;
    private String password;
    private String url;
    private String directory;
    private int timeout;
    private boolean authOn;
    
    public ConnectionProperties(String username, String password, String url, String directory, int timeout){
        this.userName = username;
        this.password = password;
        this.authOn = true;
        this.url = url;
        this.directory = directory;
        this.timeout = timeout;
    }
    public ConnectionProperties(String url, String directory, int timeout){
        this.url = url;
        this.directory = directory;
        this.timeout = timeout;
        this.authOn = false;
    }
    
    public void setAuthOn(){
        this.authOn = true;
    }
    
    public void setAuthOff(){
        this.authOn = false;
    }
    
    public boolean getAuthOn(){
        return this.authOn;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setDirectory(String directory){
        this.directory = directory;
    }
    public void setTimeout(int timeout){
        this.timeout = timeout;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
       return  this.password;
    }
    public String getUrl(){
        return this.url;
    }
    public String getDirectory(){
        return this.directory;
    }
    public int getTimeout(){
        return this.timeout;
    }
}
