package br.com.joaodddev.banking.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();
    LocalDateTime occurredAt();
    String eventType();
}