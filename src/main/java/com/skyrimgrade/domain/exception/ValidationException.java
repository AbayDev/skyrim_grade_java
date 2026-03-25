package com.skyrimgrade.domain.exception;

import java.util.List;

public class ValidationException extends DomainException {

    private final List<FieldError> errors;

    public ValidationException(List<FieldError> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public ValidationException(String field, String message) {
        super("Validation failed");
        this.errors = List.of(new FieldError(field, message));
    }

    @Override
    public Integer getHttpStatus() {
        return 400;
    }

    @Override
    public String getErrorCode() {
        return "VALIDATION";
    }

    public List<FieldError> getErrors() {
        return errors;
    }

}
