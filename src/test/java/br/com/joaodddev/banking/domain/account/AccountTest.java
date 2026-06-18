package br.com.joaodddev.banking.domain.account;

import br.com.joaodddev.banking.domain.event.DomainEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldOpenAccountWithZeroBalance() {
        Account account = Account.open("João", AccountType.CHECKING);
        assertEquals(Money.zero(), account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    void shouldCreditAccount() {
        Account account = Account.open("João", AccountType.CHECKING);
        account.credit(Money.of("500.00"));
        assertEquals(Money.of("500.00"), account.getBalance());
    }

    @Test
    void shouldDebitAccount() {
        Account account = Account.open("João", AccountType.CHECKING);
        account.credit(Money.of("500.00"));
        account.debit(Money.of("200.00"));
        assertEquals(Money.of("300.00"), account.getBalance());
    }

    @Test
    void shouldThrowWhenInsufficientFunds() {
        Account account = Account.open("João", AccountType.CHECKING);
        account.credit(Money.of("100.00"));
        assertThrows(IllegalStateException.class, () -> account.debit(Money.of("200.00")));
    }

    @Test
    void shouldCloseAccount() {
        Account account = Account.open("João", AccountType.CHECKING);
        account.close();
        assertFalse(account.isActive());
    }

    @Test
    void shouldThrowWhenOperatingOnClosedAccount() {
        Account account = Account.open("João", AccountType.CHECKING);
        account.close();
        assertThrows(IllegalStateException.class, () -> account.credit(Money.of("100.00")));
    }

    @Test
    void shouldRegisterDomainEventsOnOpen() {
        Account account = Account.open("João", AccountType.CHECKING);
        List<DomainEvent> events = account.pullDomainEvents();
        assertEquals(1, events.size());
        assertEquals("account.opened", events.get(0).eventType());
    }

    @Test
    void shouldPullAndClearDomainEvents() {
        Account account = Account.open("João", AccountType.CHECKING);
        account.credit(Money.of("100.00"));
        account.pullDomainEvents();
        assertTrue(account.pullDomainEvents().isEmpty());
    }
}