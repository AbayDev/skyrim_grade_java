package com.skyrimgrade.domain.exception;

import java.time.Instant;
import java.util.Map;

import org.slf4j.event.Level;

public abstract class AppException extends RuntimeException {

    private final Instant timestamp = Instant.now();
    private Map<String, Object> extension;

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message, Map<String, Object> extension) {
        super(message);
        this.extension = extension;
    }

    public AppException(String message, Throwable cause, Map<String, Object> extension) {
        super(message, cause);
        this.extension = extension;
    }

    abstract public int getHttpStatus();

    abstract public String getErrorCode();

    public Level getLogLevel() {
        return Level.ERROR;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getExtension() {
        return extension;
    }
}
