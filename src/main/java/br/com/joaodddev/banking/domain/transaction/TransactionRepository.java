package br.com.joaodddev.banking.domain.transaction;

import br.com.joaodddev.banking.domain.account.AccountId;
import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountId(AccountId accountId);
}