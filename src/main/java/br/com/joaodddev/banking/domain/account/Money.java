package br.com.joaodddev.banking.domain.account;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public static final Currency BRL = Currency.getInstance("BRL");

    public Money {
        if (amount == null) throw new IllegalArgumentException("Amount cannot be null");
        if (currency == null) throw new IllegalArgumentException("Currency cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount cannot be negative");
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount, BRL);
    }

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount), BRL);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO, BRL);
    }

    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isGreaterThanOrEqualTo(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void assertSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    @Override
    public String toString() {
        return currency.getSymbol() + " " + amount.toPlainString();
    }
}