package br.com.joaodddev.banking.infrastructure.persistence.transaction;

import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.Money;
import br.com.joaodddev.banking.domain.transaction.Transaction;
import br.com.joaodddev.banking.domain.transaction.TransactionId;
import br.com.joaodddev.banking.domain.transaction.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column
    private UUID sourceAccountId;

    @Column
    private UUID targetAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime occurredAt;

    public static TransactionJpaEntity fromDomain(Transaction transaction) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.id = transaction.getId().value();
        entity.sourceAccountId = transaction.getSourceAccountId() != null
                ? transaction.getSourceAccountId().value() : null;
        entity.targetAccountId = transaction.getTargetAccountId() != null
                ? transaction.getTargetAccountId().value() : null;
        entity.amount = transaction.getAmount().amount();
        entity.type = transaction.getType();
        entity.occurredAt = transaction.getOccurredAt();
        return entity;
    }

    public Transaction toDomain() {
        return Transaction.reconstitute(
                TransactionId.of(id.toString()),
                sourceAccountId != null ? AccountId.of(sourceAccountId.toString()) : null,
                targetAccountId != null ? AccountId.of(targetAccountId.toString()) : null,
                Money.of(amount),
                type,
                occurredAt
        );
    }
}