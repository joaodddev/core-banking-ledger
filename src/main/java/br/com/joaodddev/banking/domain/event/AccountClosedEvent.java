package br.com.joaodddev.banking.domain.event;

import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountClosedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        AccountId accountId
) implements DomainEvent {

    public static AccountClosedEvent of(AccountId accountId) {
        return new AccountClosedEvent(UUID.randomUUID(), LocalDateTime.now(), accountId);
    }

    @Override
    public String eventType() {
        return "account.closed";
    }
}