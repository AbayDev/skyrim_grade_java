package com.skyrimgrade.domain.exception;

import java.util.Map;

import org.slf4j.event.Level;

public abstract class DomainException extends AppException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(String message, Map<String, Object> extension) {
        super(message, extension);
    }

    public DomainException(String message, Throwable cause, Map<String, Object> extension) {
        super(message, cause, extension);
    }

    @Override
    abstract public String getErrorCode();

    @Override
    abstract public int getHttpStatus();

    @Override
    public Level getLogLevel() {
        return Level.INFO;
    }
}
