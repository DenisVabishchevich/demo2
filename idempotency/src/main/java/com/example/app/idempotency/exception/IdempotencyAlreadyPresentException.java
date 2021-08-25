package com.example.app.idempotency.exception;

public class IdempotencyAlreadyPresentException extends RuntimeException {

    public IdempotencyAlreadyPresentException() {
    }

    public IdempotencyAlreadyPresentException(String message) {
        super(message);
    }

    public IdempotencyAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdempotencyAlreadyPresentException(Throwable cause) {
        super(cause);
    }

    public IdempotencyAlreadyPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
