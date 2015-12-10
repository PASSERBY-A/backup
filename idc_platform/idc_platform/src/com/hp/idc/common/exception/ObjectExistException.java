package com.hp.idc.common.exception;

public class ObjectExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ObjectExistException(){
		super();
	}
	
	public ObjectExistException(String message){
		super(message);
	}
	
	public ObjectExistException(Throwable cause){
		super(cause);
	}
	
	public ObjectExistException(String message, Throwable cause){
		super(message,cause);
	}
	
}
