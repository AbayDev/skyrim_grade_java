package com.skyrimgrade.domain.exception;

import java.util.List;
import java.util.Map;

public class ValidationException extends DomainException {

    private static final String ERROR_KEY = "errors";

    public ValidationException(List<FieldError> errors) {
        Map<String, Object> extension = Map.of(ERROR_KEY, errors);
        super("Validation failed", extension);
    }

    public ValidationException(String field, String message) {
        Map<String, Object> extension = Map.of(ERROR_KEY, List.of(new FieldError(field, message)
        ));
        super("Validation failed", extension);
    }

    @Override
    public String getErrorCode() {
        return "VALIDATION";
    }

    @SuppressWarnings("unchecked")
    public List<FieldError> getErrors() {
        return (List<FieldError>) this.getExtension().get(ERROR_KEY);
    }

}
