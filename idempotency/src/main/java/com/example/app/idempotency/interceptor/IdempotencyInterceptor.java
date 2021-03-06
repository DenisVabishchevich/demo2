package com.example.app.idempotency.interceptor;

import com.example.app.idempotency.domain.Idempotency;
import com.example.app.idempotency.exception.IdempotencyIsEmptyException;
import com.example.app.idempotency.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    public static final String IDEMPOTENCY_KEY = "Idempotency-Key";

    private final IdempotencyService idempotencyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String idempotencyKey = request.getHeader(IDEMPOTENCY_KEY);

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IdempotencyIsEmptyException();
        }
        Idempotency idempotency = new Idempotency();
        idempotency.setIdempotencyKey(idempotencyKey);

        Optional<Idempotency> fromDb = idempotencyService.getIdempotencyByKey(idempotencyKey)
            .stream()
            .findFirst();

        if (fromDb.isPresent()) {
            response.resetBuffer();
            response.getOutputStream().print(fromDb.get().getContent());
            response.setStatus(fromDb.get().getStatus());
            response.flushBuffer();
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);

    }

}
