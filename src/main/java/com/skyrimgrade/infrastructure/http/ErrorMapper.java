package com.skyrimgrade.infrastructure.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import com.skyrimgrade.domain.exception.AppException;

public class ErrorMapper {

    public void handle(Throwable e, HttpContext ctx) throws IOException {
        // Разворачиваем InvocationTargetException от Reflection
        Throwable cause = (e instanceof InvocationTargetException ite)
                ? ite.getCause()
                : e;

        if (cause instanceof AppException ae) {
            ctx.json(
                    ae.getHttpStatus(),
                    new ErrorResponse(
                            ae.getHttpStatus(),
                            ae.getErrorCode(),
                            ae.getMessage(),
                            ae.getExtension(),
                            ae.getTimestamp()
                    )
            );
        } else {
            ctx.json(
                    500,
                    new ErrorResponse(
                            500,
                            "INTERNAL_SERVER_ERROR",
                            "Internal server error",
                            Instant.now()
                    )
            );
        }
    }
}
