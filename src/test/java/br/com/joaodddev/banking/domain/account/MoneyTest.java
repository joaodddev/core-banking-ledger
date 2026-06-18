package br.com.joaodddev.banking.domain.account;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithPositiveAmount() {
        Money money = Money.of("100.00");
        assertEquals(new BigDecimal("100.00"), money.amount());
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("-1.00"));
    }

    @Test
    void shouldAddTwoMoneyValues() {
        Money a = Money.of("100.00");
        Money b = Money.of("50.00");
        assertEquals(Money.of("150.00"), a.add(b));
    }

    @Test
    void shouldSubtractTwoMoneyValues() {
        Money a = Money.of("100.00");
        Money b = Money.of("40.00");
        assertEquals(Money.of("60.00"), a.subtract(b));
    }
}