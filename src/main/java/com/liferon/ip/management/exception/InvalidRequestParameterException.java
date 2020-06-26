package com.liferon.ip.management.exception;

import org.springframework.validation.Errors;

public class InvalidRequestParameterException extends RestException {
	private static final long serialVersionUID = 1995646745997826803L;

	public InvalidRequestParameterException(Errors err){
		super(err);
	}
	public InvalidRequestParameterException(String errorMessage){
		super(errorMessage);
	}
	public InvalidRequestParameterException(String error, String description){
		super(error, description);
	}
}
