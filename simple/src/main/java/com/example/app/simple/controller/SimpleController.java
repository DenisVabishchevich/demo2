package com.example.app.simple.controller;

import com.example.app.simple.controller.dto.MockDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    @GetMapping("/data")
    public MockDto getSomeData() {
        return MockDto.builder()
            .data("mock_data: " + UUID.randomUUID())
            .build();
    }

    @GetMapping("/errors")
    public MockDto getSomeError() {
        throw new IllegalStateException("Controller error");
    }
}
