package br.com.joaodddev.banking.interfaces.controller;

import br.com.joaodddev.banking.application.usecase.CloseAccountUseCase;
import br.com.joaodddev.banking.application.usecase.OpenAccountUseCase;
import br.com.joaodddev.banking.interfaces.dto.account.AccountResponse;
import br.com.joaodddev.banking.interfaces.dto.account.OpenAccountRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final OpenAccountUseCase openAccountUseCase;
    private final CloseAccountUseCase closeAccountUseCase;

    public AccountController(OpenAccountUseCase openAccountUseCase,
                             CloseAccountUseCase closeAccountUseCase) {
        this.openAccountUseCase = openAccountUseCase;
        this.closeAccountUseCase = closeAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> openAccount(@Valid @RequestBody OpenAccountRequest request) {
        OpenAccountUseCase.Output output = openAccountUseCase.execute(
                new OpenAccountUseCase.Input(request.ownerName(), request.accountType())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AccountResponse(output.accountId(), output.ownerName(), output.accountType(), output.active())
        );
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable String accountId) {
        CloseAccountUseCase.Output output = closeAccountUseCase.execute(
                new CloseAccountUseCase.Input(accountId)
        );
        return ResponseEntity.ok(
                new AccountResponse(output.accountId(), null, null, output.active())
        );
    }
}