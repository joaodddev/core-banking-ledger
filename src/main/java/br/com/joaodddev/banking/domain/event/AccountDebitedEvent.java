package br.com.joaodddev.banking.domain.event;

import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.Money;
import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountDebitedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        AccountId accountId,
        Money amount
) implements DomainEvent {

    public static AccountDebitedEvent of(AccountId accountId, Money amount) {
        return new AccountDebitedEvent(UUID.randomUUID(), LocalDateTime.now(), accountId, amount);
    }

    @Override
    public String eventType() {
        return "account.debited";
    }
}