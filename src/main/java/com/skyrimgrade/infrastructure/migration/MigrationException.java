package com.skyrimgrade.infrastructure.migration;

import com.skyrimgrade.domain.exception.AppException;

public class MigrationException extends AppException {

    public MigrationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "MIGRATION_FAILED";
    }
}

