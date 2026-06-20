package br.com.joaodddev.banking.infrastructure.persistence.transaction;

import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.transaction.Transaction;
import br.com.joaodddev.banking.domain.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity entity = TransactionJpaEntity.fromDomain(transaction);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public List<Transaction> findByAccountId(AccountId accountId) {
        return jpaRepository
                .findBySourceAccountIdOrTargetAccountId(accountId.value(), accountId.value())
                .stream()
                .map(TransactionJpaEntity::toDomain)
                .toList();
    }
}