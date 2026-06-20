package br.com.joaodddev.banking.application.usecase;

import br.com.joaodddev.banking.application.exception.AccountNotFoundException;
import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import br.com.joaodddev.banking.domain.event.DomainEvent;

import java.util.List;

public class CloseAccountUseCase {

    private final AccountRepository accountRepository;
    private final DomainEventPublisher eventPublisher;

    public CloseAccountUseCase(AccountRepository accountRepository, DomainEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    public Output execute(Input input) {
        AccountId accountId = AccountId.of(input.accountId());
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(input.accountId()));

        account.close();
        accountRepository.save(account);

        List<DomainEvent> events = account.pullDomainEvents();
        eventPublisher.publishAll(events);

        return new Output(account.getId().toString(), account.isActive());
    }

    public record Input(String accountId) {}

    public record Output(String accountId, boolean active) {}
}