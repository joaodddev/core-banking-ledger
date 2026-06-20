package br.com.joaodddev.banking.infrastructure.config;

import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.application.usecase.*;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import br.com.joaodddev.banking.domain.transaction.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public OpenAccountUseCase openAccountUseCase(AccountRepository accountRepository,
                                                 DomainEventPublisher eventPublisher) {
        return new OpenAccountUseCase(accountRepository, eventPublisher);
    }

    @Bean
    public CreditAccountUseCase creditAccountUseCase(AccountRepository accountRepository,
                                                     TransactionRepository transactionRepository,
                                                     DomainEventPublisher eventPublisher) {
        return new CreditAccountUseCase(accountRepository, transactionRepository, eventPublisher);
    }

    @Bean
    public DebitAccountUseCase debitAccountUseCase(AccountRepository accountRepository,
                                                   TransactionRepository transactionRepository,
                                                   DomainEventPublisher eventPublisher) {
        return new DebitAccountUseCase(accountRepository, transactionRepository, eventPublisher);
    }

    @Bean
    public TransferFundsUseCase transferFundsUseCase(AccountRepository accountRepository,
                                                     TransactionRepository transactionRepository,
                                                     DomainEventPublisher eventPublisher) {
        return new TransferFundsUseCase(accountRepository, transactionRepository, eventPublisher);
    }

    @Bean
    public CloseAccountUseCase closeAccountUseCase(AccountRepository accountRepository,
                                                   DomainEventPublisher eventPublisher) {
        return new CloseAccountUseCase(accountRepository, eventPublisher);
    }
}