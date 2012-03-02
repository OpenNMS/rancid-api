package org.opennms.rancid;


public class RancidNodeAuthentication {

	private String deviceName;
    private String user;
	private String password;
	private String enablePass;
	private boolean autoEnable; 
	private String authType;
	private Method connectionMethod;
	
	private static class Method {
		private final static String sshMethod = "ssh";
		private final static String telnetMethod = "telnet";
		private final static String rshMethod = "rsh";
		private final static String tftpMethod = "tftp";
		private final static String httpMethod = "http";
		private final static String notusedMethod = "notused";

		private String method;

		public String getMethod() {
			return method;
		}
		
		/*
		public void setMethod(String method) {
			if (method.endsWith(sshMethod)) this.method = method;
			method = telnetMethod;
		}
		
		public Method() {
			super();
			this.method = telnetMethod;
		}
		*/
		
		public Method(String method) {
		    //System.out.println("public Method(String method)"  + method);
			if (method.endsWith(sshMethod)){
			    this.method = method;
			} else if (method.endsWith(rshMethod)) {
			    this.method = rshMethod;
                        } else if (method.endsWith(tftpMethod)) {
                            this.method = tftpMethod;
                        } else if (method.endsWith(httpMethod)) {
                            this.method = httpMethod;
                        } else if (method.endsWith(notusedMethod)) {
                            this.method = notusedMethod;
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
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
}
