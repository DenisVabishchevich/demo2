package com.example.app.idempotency.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorData {
    private String message;
    private LocalDateTime created;
}
