package br.com.joaodddev.banking.application.usecase;

import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.domain.account.Account;
import br.com.joaodddev.banking.domain.account.AccountId;
import br.com.joaodddev.banking.domain.account.AccountRepository;
import br.com.joaodddev.banking.domain.account.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAccountUseCaseTest {

    private AccountRepository accountRepository;
    private DomainEventPublisher eventPublisher;
    private OpenAccountUseCase useCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        eventPublisher = mock(DomainEventPublisher.class);
        useCase = new OpenAccountUseCase(accountRepository, eventPublisher);
    }

    @Test
    void shouldOpenAccountSuccessfully() {
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OpenAccountUseCase.Output output = useCase.execute(
                new OpenAccountUseCase.Input("João", AccountType.CHECKING)
        );

        assertNotNull(output.accountId());
        assertEquals("João", output.ownerName());
        assertTrue(output.active());
        verify(accountRepository).save(any(Account.class));
        verify(eventPublisher).publishAll(anyList());
    }
}