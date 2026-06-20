package br.com.joaodddev.banking.interfaces.dto.account;

import br.com.joaodddev.banking.domain.account.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OpenAccountRequest(
        @NotBlank(message = "Owner name is required")
        String ownerName,

        @NotNull(message = "Account type is required")
        AccountType accountType
) {}