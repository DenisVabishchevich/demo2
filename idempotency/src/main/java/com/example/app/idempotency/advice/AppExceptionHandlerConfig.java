package com.example.app.idempotency.advice;

import com.example.app.idempotency.domain.Idempotency;
import com.example.app.idempotency.exception.IdempotencyIsEmptyException;
import com.example.app.idempotency.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

import static com.example.app.idempotency.interceptor.IdempotencyInterceptor.IDEMPOTENCY_KEY;

// workaround to register controller advice in common module
@Configuration
public class AppExceptionHandlerConfig {

    @RequiredArgsConstructor
    @RestControllerAdvice
    @Slf4j
    public static class AppExceptionHandler {

        private final IdempotencyService idempotencyService;

        @ExceptionHandler(IdempotencyIsEmptyException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public void onMissingIdempotencyKey(IdempotencyIsEmptyException exception) {
            log.error(exception.getMessage(), exception);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> globalException(Exception exception, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
            log.error(exception.getMessage(), exception);

            String idempotencyKey = request.getHeader(IDEMPOTENCY_KEY);
            Idempotency idempotency = new Idempotency();
            idempotency.setIdempotencyKey(idempotencyKey);

            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            String content = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            idempotency.setContent(content);
            idempotency.setStatus(400);

            idempotencyService.createIdempotency(idempotency);
            return ResponseEntity.status(400).body(content);
        }

    }

}
