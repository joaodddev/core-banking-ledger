package br.com.joaodddev.banking.application.usecase;

import br.com.joaodddev.banking.application.exception.AccountNotFoundException;
import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import br.com.joaodddev.banking.domain.account.Money;
import br.com.joaodddev.banking.domain.event.DomainEvent;
import br.com.joaodddev.banking.domain.transaction.Transaction;
import br.com.joaodddev.banking.domain.transaction.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

public class CreditAccountUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final DomainEventPublisher eventPublisher;

    public CreditAccountUseCase(AccountRepository accountRepository,
                                TransactionRepository transactionRepository,
                                DomainEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Output execute(Input input) {
        AccountId accountId = AccountId.of(input.accountId());
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(input.accountId()));

        Money amount = Money.of(input.amount());
        account.credit(amount);
        accountRepository.save(account);

        Transaction transaction = Transaction.createCredit(accountId, amount);
        transactionRepository.save(transaction);

        List<DomainEvent> events = account.pullDomainEvents();
        eventPublisher.publishAll(events);

        return new Output(transaction.getId().toString(), account.getId().toString(), account.getBalance().amount());
    }

    public record Input(String accountId, BigDecimal amount) {}

    public record Output(String transactionId, String accountId, BigDecimal newBalance) {}
}