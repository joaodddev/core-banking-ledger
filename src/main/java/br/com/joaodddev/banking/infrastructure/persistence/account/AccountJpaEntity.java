package br.com.joaodddev.banking.infrastructure.persistence.account;

import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountType;
import br.com.joaodddev.banking.domain.account.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String ownerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static AccountJpaEntity fromDomain(Account account) {
        AccountJpaEntity entity = new AccountJpaEntity();
        entity.id = account.getId().value();
        entity.ownerName = account.getOwnerName();
        entity.type = account.getType();
        entity.balance = account.getBalance().amount();
        entity.active = account.isActive();
        entity.createdAt = account.getCreatedAt();
        return entity;
    }

    public Account toDomain() {
        return Account.reconstitute(
                AccountId.of(id.toString()),
                ownerName,
                type,
                Money.of(balance),
                active,
                createdAt
        );
    }
}