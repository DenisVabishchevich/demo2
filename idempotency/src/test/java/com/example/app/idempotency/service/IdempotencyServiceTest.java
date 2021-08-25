package com.example.app.idempotency.service;

import com.example.app.idempotency.domain.Idempotency;
import com.example.app.idempotency.exception.IdempotencyAlreadyPresentException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
class IdempotencyServiceTest {

    @Autowired
    private IdempotencyService idempotencyService;

    @Test
    void saveIdempotencyTest() {
        Idempotency idempotency = new Idempotency();
        String idempotencyKey = UUID.randomUUID().toString();
        idempotency.setIdempotencyKey(idempotencyKey);

        Idempotency savedIdempotency = idempotencyService.createIdempotency(idempotency);


        Assertions.assertThat(idempotencyService.getIdempotency(savedIdempotency.getId()))
            .isNotEmpty()
            .map(Idempotency::getIdempotencyKey)
            .hasValue(idempotencyKey);

        Assertions.assertThat(idempotencyService.getIdempotencyByKey(idempotencyKey)).hasSize(1)
            .map(Idempotency::getIdempotencyKey)
            .allMatch(key -> key.equals(idempotencyKey));
    }

    @Test
    void duplicateIdempotencyId() {
        String idempotencyId = UUID.randomUUID().toString();

        Idempotency idempotency1 = new Idempotency();
        idempotency1.setIdempotencyKey(idempotencyId);

        Idempotency idempotency2 = new Idempotency();
        idempotency2.setIdempotencyKey(idempotencyId);

        idempotencyService.createIdempotency(idempotency1);
        Assertions.assertThatThrownBy(() -> {
            idempotencyService.createIdempotency(idempotency2);
        }).isInstanceOf(IdempotencyAlreadyPresentException.class);
    }

}