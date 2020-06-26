package com.liferon.ip.management.exception;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Getter
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Errors errors;
    private String error;
    private String description;

    public RestException(Errors err) {
        this.errors = err;
        unbundleError();
    }

    public RestException(String errorMessage) {
        super(errorMessage);
        this.error = errorMessage;
    }

    public RestException(String error, String description) {
        this.error = error;
        this.description = description;
    }
    
    private void unbundleError() {
        FieldError err = (FieldError) this.errors.getAllErrors().get(0);
        this.error = err.getCode();
        this.description = err.getDefaultMessage();
    }
}
