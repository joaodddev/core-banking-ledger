package br.com.joaodddev.banking.domain.account;

import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(AccountId id);
}
