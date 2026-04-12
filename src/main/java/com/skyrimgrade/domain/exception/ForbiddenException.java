package com.skyrimgrade.domain.exception;

public class ForbiddenException extends DomainException {

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public int getHttpStatus() {
        return 403;
    }

    @Override
    public String getErrorCode() {
        return "FORBIDDEN";
    }

}
