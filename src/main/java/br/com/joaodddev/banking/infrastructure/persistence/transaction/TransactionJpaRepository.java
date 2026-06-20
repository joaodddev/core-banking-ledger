package br.com.joaodddev.banking.infrastructure.persistence.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    List<TransactionJpaEntity> findBySourceAccountIdOrTargetAccountId(UUID sourceAccountId, UUID targetAccountId);
}