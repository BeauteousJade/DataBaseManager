package com.example.exception;

public class IdNotExistException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public IdNotExistException() {
		super("id is not  exist, please check whether id have a id Annotation");
	}
}
