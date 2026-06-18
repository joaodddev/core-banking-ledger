package br.com.joaodddev.banking.application.port.out;

import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {
    void publishAll(List<DomainEvent> events);
}