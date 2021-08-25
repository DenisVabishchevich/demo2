package com.example.app.idempotency.exception;

public class IdempotencyIsEmptyException extends RuntimeException {
    public IdempotencyIsEmptyException() {
    }

    public IdempotencyIsEmptyException(String message) {
        super(message);
    }

    public IdempotencyIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdempotencyIsEmptyException(Throwable cause) {
        super(cause);
    }

    public IdempotencyIsEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
