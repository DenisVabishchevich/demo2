package com.example.app.idempotency.service;

import com.example.app.idempotency.domain.Idempotency;
import com.example.app.idempotency.exception.IdempotencyAlreadyPresentException;
import com.example.app.idempotency.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    @Transactional(readOnly = true)
    public Optional<Idempotency> getIdempotency(Long id) {
        return idempotencyRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Idempotency> getIdempotencyByKey(String idempotencyKey) {
        return idempotencyRepository.findAllByIdempotencyKey(idempotencyKey);
    }

    @Transactional
    public Idempotency createIdempotency(Idempotency idempotency) {

        boolean alreadyPresent = idempotencyRepository.findAllByIdempotencyKey(idempotency.getIdempotencyKey())
            .stream()
            .findFirst()
            .isPresent();

        if (!alreadyPresent) {
            return idempotencyRepository.save(idempotency);
        } else {
            throw new IdempotencyAlreadyPresentException();
        }

    }
}
