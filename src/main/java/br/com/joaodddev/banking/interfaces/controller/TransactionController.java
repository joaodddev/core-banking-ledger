package br.com.joaodddev.banking.interfaces.controller;

import br.com.joaodddev.banking.application.usecase.CreditAccountUseCase;
import br.com.joaodddev.banking.application.usecase.DebitAccountUseCase;
import br.com.joaodddev.banking.application.usecase.TransferFundsUseCase;
import br.com.joaodddev.banking.interfaces.dto.transaction.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class TransactionController {

    private final CreditAccountUseCase creditAccountUseCase;
    private final DebitAccountUseCase debitAccountUseCase;
    private final TransferFundsUseCase transferFundsUseCase;

    public TransactionController(CreditAccountUseCase creditAccountUseCase,
                                 DebitAccountUseCase debitAccountUseCase,
                                 TransferFundsUseCase transferFundsUseCase) {
        this.creditAccountUseCase = creditAccountUseCase;
        this.debitAccountUseCase = debitAccountUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
    }

    @PostMapping("/{accountId}/credit")
    public ResponseEntity<TransactionResponse> credit(@PathVariable String accountId,
                                                      @Valid @RequestBody CreditDebitRequest request) {
        CreditAccountUseCase.Output output = creditAccountUseCase.execute(
                new CreditAccountUseCase.Input(accountId, request.amount())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransactionResponse(output.transactionId(), output.accountId(), output.newBalance())
        );
    }

    @PostMapping("/{accountId}/debit")
    public ResponseEntity<TransactionResponse> debit(@PathVariable String accountId,
                                                     @Valid @RequestBody CreditDebitRequest request) {
        DebitAccountUseCase.Output output = debitAccountUseCase.execute(
                new DebitAccountUseCase.Input(accountId, request.amount())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransactionResponse(output.transactionId(), output.accountId(), output.newBalance())
        );
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<TransferResponse> transfer(@PathVariable String accountId,
                                                     @Valid @RequestBody TransferRequest request) {
        TransferFundsUseCase.Output output = transferFundsUseCase.execute(
                new TransferFundsUseCase.Input(accountId, request.targetAccountId(), request.amount())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransferResponse(output.transactionId(), output.sourceAccountId(), output.targetAccountId(), output.amount())
        );
    }
}