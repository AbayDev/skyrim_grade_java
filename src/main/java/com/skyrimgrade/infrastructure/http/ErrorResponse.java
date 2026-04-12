package com.skyrimgrade.infrastructure.http;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    public final int httpStatus;
    public final String errorCode;
    public final String message;
    public final Map<String, Object> extension;
    public final Instant timestamp;

    public ErrorResponse(
            int httpStatus,
            String errorCode,
            String message,
            Map<String, Object> extension,
            Instant timestamp
    ) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
        this.extension = extension;
        this.timestamp = timestamp;
    }

    public ErrorResponse(int httpStatus, String errorCode, String message, Instant timestamp) {
        this(httpStatus, errorCode, message, null, timestamp);
    }

}
