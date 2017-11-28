package com.example.exception;

/**
 * Id 值为空的异常
 * @author pby
 */
public class IdIsNullException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public IdIsNullException() {
		super("id is null");
	}
	
	
}
