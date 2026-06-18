package br.com.joaodddev.banking.domain.transaction;

import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.Money;
import java.time.LocalDateTime;

public class Transaction {

    private final TransactionId id;
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final Money amount;
    private final TransactionType type;
    private final LocalDateTime occurredAt;

    private Transaction(TransactionId id, AccountId sourceAccountId, AccountId targetAccountId,
                        Money amount, TransactionType type, LocalDateTime occurredAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.type = type;
        this.occurredAt = occurredAt;
    }

    public static Transaction createTransfer(AccountId sourceAccountId, AccountId targetAccountId, Money amount) {
        if (amount == null || !amount.isPositive()) throw new IllegalArgumentException("Transfer amount must be positive");
        if (sourceAccountId.equals(targetAccountId)) throw new IllegalArgumentException("Source and target accounts must differ");
        return new Transaction(TransactionId.generate(), sourceAccountId, targetAccountId, amount, TransactionType.TRANSFER, LocalDateTime.now());
    }

    public static Transaction createCredit(AccountId targetAccountId, Money amount) {
        if (amount == null || !amount.isPositive()) throw new IllegalArgumentException("Credit amount must be positive");
        return new Transaction(TransactionId.generate(), null, targetAccountId, amount, TransactionType.CREDIT, LocalDateTime.now());
    }

    public static Transaction createDebit(AccountId sourceAccountId, Money amount) {
        if (amount == null || !amount.isPositive()) throw new IllegalArgumentException("Debit amount must be positive");
        return new Transaction(TransactionId.generate(), sourceAccountId, null, amount, TransactionType.DEBIT, LocalDateTime.now());
    }

    public TransactionId getId() { return id; }
    public AccountId getSourceAccountId() { return sourceAccountId; }
    public AccountId getTargetAccountId() { return targetAccountId; }
    public Money getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}