package com.squadio.assessment.exceptions;

public class ForbbidenAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ForbbidenAccessException(){}
	
	public ForbbidenAccessException(String message){
		super(message);
	}

}
