package com.liferon.ip.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BadRequestException extends RestException {
    public BadRequestException(Errors err) {
        super(err);
    }
}
