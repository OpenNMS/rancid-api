package org.opennms.rancid;

public class RancidApiException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4501432941276162589L;

	RancidApiException(String errorMessage){
		super(errorMessage);
	}
	
}
