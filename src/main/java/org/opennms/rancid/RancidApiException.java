package org.opennms.rancid;

public class RancidApiException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4501432941276162589L;
	
	private int code=0;
	
    public static int RWS_BUSY =    1;
    public static int RWS_RESOURCE_NOT_FOUND = 2;
    public static int RWS_TIMEOUT = 3;
    public static int RWS_AUTH_FAILES = 4;

    public static int OTHER_ERROR =   999;

    public int getRancidCode() {
        return code;
    }
	
	RancidApiException(String errorMessage, int code) {
	    super(errorMessage);
	    this.code = code;
	}

	RancidApiException(String errorMessage){
		super(errorMessage);
		this.code = OTHER_ERROR;
	}
	
}
