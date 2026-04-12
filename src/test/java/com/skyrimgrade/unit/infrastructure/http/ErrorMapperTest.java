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
import static org.mockito.Mockito.when;

import com.skyrimgrade.domain.exception.AppException;
import com.skyrimgrade.infrastructure.http.ErrorMapper;
import com.skyrimgrade.infrastructure.http.ErrorResponse;
import com.skyrimgrade.infrastructure.http.HttpContext;
import com.skyrimgrade.infrastructure.http.HttpStatusResolver;

class ErrorMapperTest {

    private ErrorMapper errorMapper;
    private HttpContext ctx;
    private HttpStatusResolver httpStatusResolver;

    @BeforeEach
    void setUp() {
        httpStatusResolver = mock(HttpStatusResolver.class);
        errorMapper = new ErrorMapper(httpStatusResolver);
        ctx = mock(HttpContext.class);
    }

    // ─── AppException ─────────────────────────────────────────────────────────

    @Test
    void handle_shouldRespondWithAppExceptionStatusAndCode() throws IOException {
        // given
        AppException appEx = new AppException("Not found") {
            @Override public String getErrorCode() { return "NOT_FOUND"; }
        };
        when(httpStatusResolver.resolve(appEx)).thenReturn(404);
        ArgumentCaptor<ErrorResponse> bodyCaptor = ArgumentCaptor.forClass(ErrorResponse.class);

        // when
        errorMapper.handle(appEx, ctx);

        // then
        verify(ctx).json(eq(404), bodyCaptor.capture());
        ErrorResponse response = bodyCaptor.getValue();
        assertThat(response.errorCode).isEqualTo("NOT_FOUND");
        assertThat(response.message).isEqualTo("Not found");
    }

    @Test
    void handle_shouldIncludeExtension_whenAppExceptionHasExtension() throws IOException {
        // given
        Map<String, Object> ext = Map.of("field", "email");
        AppException appEx = new AppException("Validation failed", ext) {
            @Override public String getErrorCode() { return "VALIDATION_ERROR"; }
        };
        when(httpStatusResolver.resolve(appEx)).thenReturn(422);
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
            @Override public String getErrorCode() { return "FORBIDDEN"; }
        };
        when(httpStatusResolver.resolve(appEx)).thenReturn(403);
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
        assertThat(bodyCaptor.getValue().errorCode).isEqualTo("INTERNAL_SERVER_ERROR");
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
