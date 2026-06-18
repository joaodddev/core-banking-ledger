package br.com.joaodddev.banking.domain.transaction;

import java.util.UUID;

public record TransactionId(UUID value) {

    public TransactionId {
        if (value == null) throw new IllegalArgumentException("TransactionId cannot be null");
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID());
    }

    public static TransactionId of(String value) {
        return new TransactionId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}