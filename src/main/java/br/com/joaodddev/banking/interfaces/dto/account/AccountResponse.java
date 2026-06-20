package br.com.joaodddev.banking.interfaces.dto.account;

import br.com.joaodddev.banking.domain.account.AccountType;

public record AccountResponse(
        String accountId,
        String ownerName,
        AccountType accountType,
        boolean active
) {}