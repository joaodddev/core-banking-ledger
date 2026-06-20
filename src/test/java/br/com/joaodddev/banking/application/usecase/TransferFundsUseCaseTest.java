package br.com.joaodddev.banking.application.usecase;

import br.com.joaodddev.banking.application.exception.AccountNotFoundException;
import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import br.com.joaodddev.banking.domain.account.AccountType;
import br.com.joaodddev.banking.domain.account.Money;
import br.com.joaodddev.banking.domain.transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferFundsUseCaseTest {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private DomainEventPublisher eventPublisher;
    private TransferFundsUseCase useCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        eventPublisher = mock(DomainEventPublisher.class);
        useCase = new TransferFundsUseCase(accountRepository, transactionRepository, eventPublisher);
    }

    @Test
    void shouldTransferFundsSuccessfully() {
        Account source = Account.open("João", AccountType.CHECKING);
        source.credit(Money.of("500.00"));
        source.pullDomainEvents();

        Account target = Account.open("Maria", AccountType.CHECKING);
        target.pullDomainEvents();

        when(accountRepository.findById(source.getId())).thenReturn(Optional.of(source));
        when(accountRepository.findById(target.getId())).thenReturn(Optional.of(target));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransferFundsUseCase.Output output = useCase.execute(
                new TransferFundsUseCase.Input(
                        source.getId().toString(),
                        target.getId().toString(),
                        new BigDecimal("200.00")
                )
        );

        assertNotNull(output.transactionId());
        assertEquals(Money.of("300.00"), source.getBalance());
        assertEquals(Money.of("200.00"), target.getBalance());
        verify(eventPublisher).publishAll(anyList());
    }

    @Test
    void shouldThrowWhenSourceAccountNotFound() {
        when(accountRepository.findById(any(AccountId.class))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> useCase.execute(
                new TransferFundsUseCase.Input(
                        "00000000-0000-0000-0000-000000000000",
                        "00000000-0000-0000-0000-000000000001",
                        new BigDecimal("100.00")
                )
        ));
    }
}