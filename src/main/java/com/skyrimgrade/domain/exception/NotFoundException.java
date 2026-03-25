package com.skyrimgrade.domain.exception;

public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException of(String entity, Object id) {
        return new NotFoundException(
                "Entity " + entity + " with id " + id + " not found"
        );
    }

    @Override
    public Integer getHttpStatus() {
        return 404;
    }

    @Override
    public String getErrorCode() {
        return "NOT_FOUND";
    }

}
