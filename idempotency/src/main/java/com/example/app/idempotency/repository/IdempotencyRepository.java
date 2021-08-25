package com.example.app.idempotency.repository;

import com.example.app.idempotency.domain.Idempotency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IdempotencyRepository extends CrudRepository<Idempotency, Long> {

    List<Idempotency> findAllByIdempotencyKey(String idempotencyKey);
}
