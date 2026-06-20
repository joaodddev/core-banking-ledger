package br.com.joaodddev.banking.interfaces.dto.transaction;

import java.math.BigDecimal;

public record TransferResponse(
        String transactionId,
        String sourceAccountId,
        String targetAccountId,
        BigDecimal amount
) {}