package com.example.exception;

public class IdIsNullException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public IdIsNullException() {
		super("id is null");
	}
	
	
}
