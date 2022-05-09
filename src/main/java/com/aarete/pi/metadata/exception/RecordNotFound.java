package com.aarete.pi.metadata.exception;

public class RecordNotFound extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7523779371259821809L;

	private String message;

	public RecordNotFound(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
