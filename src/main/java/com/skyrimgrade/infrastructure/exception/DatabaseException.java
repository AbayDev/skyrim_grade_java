package com.skyrimgrade.infrastructure.exception;

import com.skyrimgrade.domain.exception.AppException;

public class DatabaseException extends AppException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "DATABASE";
    }
}
