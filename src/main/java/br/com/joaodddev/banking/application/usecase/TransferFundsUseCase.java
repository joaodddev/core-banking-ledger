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
import java.util.ArrayList;
import java.util.List;

public class TransferFundsUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final DomainEventPublisher eventPublisher;

    public TransferFundsUseCase(AccountRepository accountRepository,
                                TransactionRepository transactionRepository,
                                DomainEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Output execute(Input input) {
        AccountId sourceId = AccountId.of(input.sourceAccountId());
        AccountId targetId = AccountId.of(input.targetAccountId());

        Account source = accountRepository.findById(sourceId)
                .orElseThrow(() -> new AccountNotFoundException(input.sourceAccountId()));

        Account target = accountRepository.findById(targetId)
                .orElseThrow(() -> new AccountNotFoundException(input.targetAccountId()));

        Money amount = Money.of(input.amount());

        source.debit(amount);
        target.credit(amount);

        accountRepository.save(source);
        accountRepository.save(target);

        Transaction transaction = Transaction.createTransfer(sourceId, targetId, amount);
        transactionRepository.save(transaction);

        List<DomainEvent> events = new ArrayList<>();
        events.addAll(source.pullDomainEvents());
        events.addAll(target.pullDomainEvents());
        eventPublisher.publishAll(events);

        return new Output(
                transaction.getId().toString(),
                source.getId().toString(),
                target.getId().toString(),
                input.amount()
        );
    }

    public record Input(String sourceAccountId, String targetAccountId, BigDecimal amount) {}

    public record Output(String transactionId, String sourceAccountId, String targetAccountId, BigDecimal amount) {}
}