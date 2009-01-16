package org.opennms.rancid;


public class RancidNodeAuthentication extends RancidNode{

	private String user;
	private String password;
	private String enablePass;
	private boolean autoEnable; 
	private String authType;
	private Method connectionMethod;
	
	private class Method {
		private final static String sshMethod = "ssh";
		private final static String telnetMethod = "telnet";
		private String method;

		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			if (method.endsWith(sshMethod)) this.method = method;
			method = telnetMethod;
		}
		
		public Method() {
			super();
			this.method = telnetMethod;
		}
		
		public Method(String method) {
		    //System.out.println("public Method(String method)"  + method);
			if (method.endsWith(sshMethod)){
			    this.method = method;
			} else {
			    this.method = telnetMethod;		
			}
		}
		
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEnablePass() {
		return enablePass;
	}

	public void setEnablePass(String enablePass) {
		this.enablePass = enablePass;
	}

	public boolean isAutoEnable() {
		return autoEnable;
	}

	public void setAutoEnable(boolean autoEnable) {
		this.autoEnable = autoEnable;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public Method getConnectionMethod() {
		return connectionMethod;
	}
	
	public String getConnectionMethodString() {
	    return connectionMethod.getMethod();
	}


	public void setConnectionMethod(Method connectionMethod) {
		this.connectionMethod = connectionMethod;
	}
	
	public void setConnectionMethod(String connectionMethod) {
	    this.connectionMethod = new Method(connectionMethod);
	}
    
    public RancidNodeAuthentication(String groupName, String deviceName){
        super(groupName,deviceName);
    }
    public RancidNodeAuthentication(){
        super();
    }

}
