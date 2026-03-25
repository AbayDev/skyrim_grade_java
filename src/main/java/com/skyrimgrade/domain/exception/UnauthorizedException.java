package com.skyrimgrade.domain.exception;

public class UnauthorizedException extends DomainException {

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public Integer getHttpStatus() {
        return 401;
    }

    @Override
    public String getErrorCode() {
        return "UNAUTHORIZED";
    }

}
