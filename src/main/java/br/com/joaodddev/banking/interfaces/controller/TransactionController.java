package br.com.joaodddev.banking.interfaces.controller;

import br.com.joaodddev.banking.application.usecase.CreditAccountUseCase;
import br.com.joaodddev.banking.application.usecase.DebitAccountUseCase;
import br.com.joaodddev.banking.application.usecase.TransferFundsUseCase;
import br.com.joaodddev.banking.infrastructure.idempotency.IdempotencyService;
import br.com.joaodddev.banking.interfaces.dto.transaction.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/accounts")
public class TransactionController {

    private final CreditAccountUseCase creditAccountUseCase;
    private final DebitAccountUseCase debitAccountUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final IdempotencyService idempotencyService;

    public TransactionController(CreditAccountUseCase creditAccountUseCase,
                                 DebitAccountUseCase debitAccountUseCase,
                                 TransferFundsUseCase transferFundsUseCase,
                                 IdempotencyService idempotencyService) {
        this.creditAccountUseCase = creditAccountUseCase;
        this.debitAccountUseCase = debitAccountUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping("/{accountId}/credit")
    public ResponseEntity<TransactionResponse> credit(@PathVariable String accountId,
                                                      @RequestHeader("Idempotency-Key") String idempotencyKey,
                                                      @Valid @RequestBody CreditDebitRequest request) {
        checkIdempotency(idempotencyKey);
        CreditAccountUseCase.Output output = creditAccountUseCase.execute(
                new CreditAccountUseCase.Input(accountId, request.amount())
        );
        idempotencyService.markAsProcessed(idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransactionResponse(output.transactionId(), output.accountId(), output.newBalance())
        );
    }

    @PostMapping("/{accountId}/debit")
    public ResponseEntity<TransactionResponse> debit(@PathVariable String accountId,
                                                     @RequestHeader("Idempotency-Key") String idempotencyKey,
                                                     @Valid @RequestBody CreditDebitRequest request) {
        checkIdempotency(idempotencyKey);
        DebitAccountUseCase.Output output = debitAccountUseCase.execute(
                new DebitAccountUseCase.Input(accountId, request.amount())
        );
        idempotencyService.markAsProcessed(idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransactionResponse(output.transactionId(), output.accountId(), output.newBalance())
        );
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<TransferResponse> transfer(@PathVariable String accountId,
                                                     @RequestHeader("Idempotency-Key") String idempotencyKey,
                                                     @Valid @RequestBody TransferRequest request) {
        checkIdempotency(idempotencyKey);
        TransferFundsUseCase.Output output = transferFundsUseCase.execute(
                new TransferFundsUseCase.Input(accountId, request.targetAccountId(), request.amount())
        );
        idempotencyService.markAsProcessed(idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransferResponse(output.transactionId(), output.sourceAccountId(), output.targetAccountId(), output.amount())
        );
    }

    private void checkIdempotency(String key) {
        if (idempotencyService.isAlreadyProcessed(key)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request already processed: " + key);
        }
    }
}