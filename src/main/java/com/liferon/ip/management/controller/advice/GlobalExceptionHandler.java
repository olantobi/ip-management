package com.liferon.ip.management.controller.advice;

import com.liferon.ip.management.dto.ApiResponseDto;
import com.liferon.ip.management.exception.InternalServerErrorException;
import com.liferon.ip.management.exception.InvalidRequestParameterException;
import com.liferon.ip.management.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponseDto handleNotReadableException(HttpMessageNotReadableException exception) {
        return responseApiFactory(exception);
    }

    @ExceptionHandler(InvalidRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto invalidRequestParameter(InvalidRequestParameterException exception) {
        return responseApiFactory(exception.getMessage());
    }
    
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseDto internalServerError(InternalServerErrorException exception) {
        return responseApiFactory(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ApiResponseDto handle(final IllegalArgumentException exception) {
        return responseApiFactory(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    ApiResponseDto handle(final ResourceNotFoundException exception) {
        return responseApiFactory(exception.getMessage());
    }

    private ApiResponseDto responseApiFactory(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        
        return ApiResponseDto.builder()
                .successful(false)
                .message(exception.getMessage())
                .build();
    }

    private ApiResponseDto responseApiFactory(String message) {
        log.error(message);

        return ApiResponseDto.builder()
                .successful(false)
                .message(message)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
