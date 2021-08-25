package com.example.app.idempotency.config;

import com.example.app.idempotency.repository.IdempotencyRepository;
import com.example.app.idempotency.service.IdempotencyService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.example.app.idempotency.repository"})
@EntityScan(basePackages = {"com.example.app.idempotency.domain"})
public class Config {

    @Bean
    IdempotencyService idempotencyService(IdempotencyRepository idempotencyRepository) {
        return new IdempotencyService(idempotencyRepository);
    }

}
