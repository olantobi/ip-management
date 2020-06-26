/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.ip.management.exception;

import org.springframework.validation.Errors;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class InternalServerErrorException extends RestException {
    public InternalServerErrorException(Errors err) {
        super(err);
    }
    public InternalServerErrorException(String errorMessage) {
        super(errorMessage);
    }
    public InternalServerErrorException(String error, String description) {
        super(error, description);
    }
    
}
