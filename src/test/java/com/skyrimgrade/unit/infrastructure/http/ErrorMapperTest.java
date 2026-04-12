package com.skyrimgrade.unit.infrastructure.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.skyrimgrade.domain.exception.AppException;
import com.skyrimgrade.infrastructure.http.ErrorMapper;
import com.skyrimgrade.infrastructure.http.ErrorResponse;
import com.skyrimgrade.infrastructure.http.HttpContext;

class ErrorMapperTest {

    private ErrorMapper errorMapper;
    private HttpContext ctx;

    @BeforeEach
    void setUp() {
        errorMapper = new ErrorMapper();
        ctx = mock(HttpContext.class);
    }

    // ─── AppException ─────────────────────────────────────────────────────────

    @Test
    void handle_shouldRespondWithAppExceptionStatusAndCode() throws IOException {
        // given
        AppException appEx = new AppException("Not found") {
            @Override public int getHttpStatus() { return 404; }
            @Override public String getErrorCode() { return "NOT_FOUND"; }
        };
        ArgumentCaptor<ErrorResponse> bodyCaptor = ArgumentCaptor.forClass(ErrorResponse.class);

        // when
        errorMapper.handle(appEx, ctx);

        // then
        verify(ctx).json(eq(404), bodyCaptor.capture());
        ErrorResponse response = bodyCaptor.getValue();
        assertThat(response.httpStatus).isEqualTo(404);
        assertThat(response.errorCode).isEqualTo("NOT_FOUND");
        assertThat(response.message).isEqualTo("Not found");
    }

    @Test
    void handle_shouldIncludeExtension_whenAppExceptionHasExtension() throws IOException {
        // given
        Map<String, Object> ext = Map.of("field", "email");
        AppException appEx = new AppException("Validation failed", ext) {
            @Override public int getHttpStatus() { return 422; }
            @Override public String getErrorCode() { return "VALIDATION_ERROR"; }
        };
        ArgumentCaptor<ErrorResponse> bodyCaptor = ArgumentCaptor.forClass(ErrorResponse.class);

        // when
        errorMapper.handle(appEx, ctx);

        // then
        verify(ctx).json(eq(422), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue().extension).containsEntry("field", "email");
    }

    // ─── InvocationTargetException ────────────────────────────────────────────

    @Test
    void handle_shouldUnwrapInvocationTargetException_whenCauseIsAppException() throws IOException {
        // given
        AppException appEx = new AppException("Forbidden") {
            @Override public int getHttpStatus() { return 403; }
            @Override public String getErrorCode() { return "FORBIDDEN"; }
        };
        InvocationTargetException wrapped = new InvocationTargetException(appEx);
        ArgumentCaptor<ErrorResponse> bodyCaptor = ArgumentCaptor.forClass(ErrorResponse.class);

        // when
        errorMapper.handle(wrapped, ctx);

        // then
        verify(ctx).json(eq(403), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue().errorCode).isEqualTo("FORBIDDEN");
    }

    // ─── Generic exception ────────────────────────────────────────────────────

    @Test
    void handle_shouldRespondWith500_forUnknownException() throws IOException {
        // given
        RuntimeException ex = new RuntimeException("Something went wrong");
        ArgumentCaptor<ErrorResponse> bodyCaptor = ArgumentCaptor.forClass(ErrorResponse.class);

        // when
        errorMapper.handle(ex, ctx);

        // then
        verify(ctx).json(eq(500), bodyCaptor.capture());
        ErrorResponse response = bodyCaptor.getValue();
        assertThat(response.httpStatus).isEqualTo(500);
        assertThat(response.errorCode).isEqualTo("INTERNAL_SERVER_ERROR");
    }

    @Test
    void handle_shouldRespondWith500_forWrappedUnknownException() throws IOException {
        // given
        InvocationTargetException wrapped = new InvocationTargetException(new NullPointerException("null"));
        ArgumentCaptor<ErrorResponse> bodyCaptor = ArgumentCaptor.forClass(ErrorResponse.class);

        // when
        errorMapper.handle(wrapped, ctx);

        // then
        verify(ctx).json(eq(500), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue().errorCode).isEqualTo("INTERNAL_SERVER_ERROR");
    }
}
