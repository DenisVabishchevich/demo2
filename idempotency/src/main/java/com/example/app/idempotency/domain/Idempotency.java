package com.example.app.idempotency.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Idempotency {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String idempotencyKey;

    private String content;

    private int status;
}
