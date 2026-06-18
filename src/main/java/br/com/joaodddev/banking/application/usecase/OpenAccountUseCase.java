package br.com.joaodddev.banking.application.usecase;

import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import br.com.joaodddev.banking.domain.account.AccountType;
import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.util.List;

public class OpenAccountUseCase {

    private final AccountRepository accountRepository;
    private final DomainEventPublisher eventPublisher;

    public OpenAccountUseCase(AccountRepository accountRepository, DomainEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    public Output execute(Input input) {
        Account account = Account.open(input.ownerName(), input.accountType());
        accountRepository.save(account);

        List<DomainEvent> events = account.pullDomainEvents();
        eventPublisher.publishAll(events);

        return new Output(account.getId().toString(), account.getOwnerName(), account.getType(), account.isActive());
    }

    public record Input(String ownerName, AccountType accountType) {}

    public record Output(String accountId, String ownerName, AccountType accountType, boolean active) {}
}