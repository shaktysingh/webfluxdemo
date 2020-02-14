package com.webservice.webfluxdemo.exception;

public class MyCustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -815700034890520023L;
	  
	public MyCustomException(String message) 
	    { 
	        // Call constructor of parent Exception 
	        super(message); 
	    } 
}
