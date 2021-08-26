package com.example.app.idempotency.advice;

import com.example.app.idempotency.domain.Idempotency;
import com.example.app.idempotency.dto.ErrorData;
import com.example.app.idempotency.service.IdempotencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.app.idempotency.interceptor.IdempotencyInterceptor.IDEMPOTENCY_KEY;

// workaround to register advices
@Configuration
public class AppAdviceConfig {

    @RequiredArgsConstructor
    @RestControllerAdvice
    @Slf4j
    public static class AppExceptionHandler {

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @SneakyThrows
        public ErrorData handleError(Exception exception, HttpServletRequest request) {
            log.error(exception.getMessage(), exception);

            return ErrorData.builder()
                .created(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        }

    }

    @ControllerAdvice
    @RequiredArgsConstructor
    @Slf4j
    public static class IdempotencyResponseAdvice implements ResponseBodyAdvice<Object> {

        private final ObjectMapper mapper;
        private final IdempotencyService idempotencyService;

        @Override
        public boolean supports(final MethodParameter returnType,
                                final Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }

        @Override
        @SneakyThrows
        public Object beforeBodyWrite(final Object body,
                                      final MethodParameter returnType,
                                      final MediaType selectedContentType,
                                      final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                      final ServerHttpRequest request,
                                      final ServerHttpResponse response) {

            List<String> idempotencyKeys = request.getHeaders().get(IDEMPOTENCY_KEY);
            if (idempotencyKeys != null && !idempotencyKeys.isEmpty()) {
                HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
                Idempotency idempotency = new Idempotency();
                idempotency.setIdempotencyKey(idempotencyKeys.get(0));
                idempotency.setContent(mapper.writeValueAsString(body));
                idempotency.setStatus(httpResponse.getStatus());
                idempotencyService.createIdempotency(idempotency);
            } else {
                log.warn("Idempotency key not present in response");
            }
            return body;
        }
    }

}
