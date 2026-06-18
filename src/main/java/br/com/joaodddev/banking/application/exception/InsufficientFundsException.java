package br.com.joaodddev.banking.application.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountId) {
        super("Insufficient funds on account: " + accountId);
    }
}