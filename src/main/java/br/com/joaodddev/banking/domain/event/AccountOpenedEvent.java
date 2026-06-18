package br.com.joaodddev.banking.domain.event;

import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountType;
import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountOpenedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        AccountId accountId,
        String ownerName,
        AccountType accountType
) implements DomainEvent {

    public static AccountOpenedEvent of(AccountId accountId, String ownerName, AccountType accountType) {
        return new AccountOpenedEvent(UUID.randomUUID(), LocalDateTime.now(), accountId, ownerName, accountType);
    }

    @Override
    public String eventType() {
        return "account.opened";
    }
}