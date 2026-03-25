package com.skyrimgrade.infrastructure.exception;

import com.skyrimgrade.domain.exception.AppException;

public class DatabaseException extends AppException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Integer getHttpStatus() {
        return 503;
    }

    @Override
    public String getErrorCode() {
        return "DATABASE";
    }
}
