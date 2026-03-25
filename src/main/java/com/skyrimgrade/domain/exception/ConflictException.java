package com.skyrimgrade.domain.exception;

public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super(message);
    }

    @Override
    public Integer getHttpStatus() {
        return 409;
    }

    @Override
    public String getErrorCode() {
        return "CONFLICT";
    }

}
