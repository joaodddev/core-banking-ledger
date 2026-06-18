package br.com.joaodddev.banking.domain.account;

import br.com.joaodddev.banking.domain.event.AccountClosedEvent;
import br.com.joaodddev.banking.domain.event.AccountCreditedEvent;
import br.com.joaodddev.banking.domain.event.AccountDebitedEvent;
import br.com.joaodddev.banking.domain.event.AccountOpenedEvent;
import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private final AccountId id;
    private final String ownerName;
    private final AccountType type;
    private Money balance;
    private boolean active;
    private final LocalDateTime createdAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Account(AccountId id, String ownerName, AccountType type, Money balance, LocalDateTime createdAt) {
        this.id = id;
        this.ownerName = ownerName;
        this.type = type;
        this.balance = balance;
        this.active = true;
        this.createdAt = createdAt;
    }

    public static Account open(String ownerName, AccountType type) {
        if (ownerName == null || ownerName.isBlank()) throw new IllegalArgumentException("Owner name is required");
        if (type == null) throw new IllegalArgumentException("Account type is required");

        Account account = new Account(AccountId.generate(), ownerName, type, Money.zero(), LocalDateTime.now());
        account.registerEvent(AccountOpenedEvent.of(account.id, ownerName, type));
        return account;
    }

    public void credit(Money amount) {
        assertActive();
        if (!amount.isPositive()) throw new IllegalArgumentException("Credit amount must be positive");
        this.balance = this.balance.add(amount);
        registerEvent(AccountCreditedEvent.of(this.id, amount));
    }

    public void debit(Money amount) {
        assertActive();
        if (!amount.isPositive()) throw new IllegalArgumentException("Debit amount must be positive");
        if (!this.balance.isGreaterThanOrEqualTo(amount)) throw new IllegalStateException("Insufficient funds");
        this.balance = this.balance.subtract(amount);
        registerEvent(AccountDebitedEvent.of(this.id, amount));
    }

    public void close() {
        assertActive();
        this.active = false;
        registerEvent(AccountClosedEvent.of(this.id));
    }

    private void assertActive() {
        if (!this.active) throw new IllegalStateException("Account is not active");
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

    public AccountId getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public AccountType getType() { return type; }
    public Money getBalance() { return balance; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
