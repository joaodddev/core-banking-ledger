package br.com.joaodddev.banking.interfaces.dto.transaction;

import java.math.BigDecimal;

public record TransactionResponse(
        String transactionId,
        String accountId,
        BigDecimal newBalance
) {}