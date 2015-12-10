package com.hp.core.exception;

/**
 * 
 * @author Zhefang Chen
 *
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public BaseException() {
		super();
	}
	
	public BaseException(String message) {
		super(message);
	}
}
