package br.com.joaodddev.banking.infrastructure.persistence.account;

import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    public AccountRepositoryAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = AccountJpaEntity.fromDomain(account);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return jpaRepository.findById(id.value()).map(AccountJpaEntity::toDomain);
    }
}