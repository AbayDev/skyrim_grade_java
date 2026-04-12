package com.skyrimgrade.domain.exception;

import java.util.List;
import java.util.Map;

public class ValidationException extends DomainException {

    public ValidationException(List<FieldError> errors) {
        Map<String, Object> extension = Map.of("errors", errors);
        super("Validation failed", extension);
    }

    public ValidationException(String field, String message) {
        Map<String, Object> extension = Map.of("errors", List.of(new FieldError(field, message)));
        super("Validation failed", extension);
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }

    @Override
    public String getErrorCode() {
        return "VALIDATION";
    }

    public List<FieldError> getErrors() {
        return (List<FieldError>) this.getExtension().get("errors");
    }

}
