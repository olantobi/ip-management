package com.liferon.ip.management.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestValidator {
    private final Validator validator;

    public RequestValidator() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public void validate(Object object) throws IllegalArgumentException {
        final Set<ConstraintViolation<Object>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            final String errors = violations
                .stream()
                .map(this::extractErrorMessage)
                .collect(Collectors.joining("\n"));

            throw new IllegalArgumentException(errors);
        }
    }

    private String extractErrorMessage(final ConstraintViolation<Object> violation) {
        return violation.getMessageTemplate().startsWith("{javax.validation.") ? violation.getPropertyPath() + " " + violation.getMessage() : violation.getMessageTemplate();
    }
}
