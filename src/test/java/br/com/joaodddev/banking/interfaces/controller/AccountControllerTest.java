package br.com.joaodddev.banking.interfaces.controller;

import br.com.joaodddev.banking.application.usecase.CloseAccountUseCase;
import br.com.joaodddev.banking.application.usecase.OpenAccountUseCase;
import br.com.joaodddev.banking.domain.account.AccountType;
import br.com.joaodddev.banking.interfaces.controller.AccountController;
import br.com.joaodddev.banking.interfaces.dto.account.OpenAccountRequest;
import br.com.joaodddev.banking.interfaces.dto.account.AccountResponse;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Test
    void shouldOpenAccountAndReturn201() {
        OpenAccountUseCase openAccountUseCase = mock(OpenAccountUseCase.class);
        CloseAccountUseCase closeAccountUseCase = mock(CloseAccountUseCase.class);

        when(openAccountUseCase.execute(any())).thenReturn(
                new OpenAccountUseCase.Output("abc-123", "João", AccountType.CHECKING, true)
        );

        AccountController controller = new AccountController(openAccountUseCase, closeAccountUseCase);

        OpenAccountRequest request = new OpenAccountRequest("João", AccountType.CHECKING);
        var responseEntity = controller.openAccount(request);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(201);
        AccountResponse body = responseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body.accountId()).isEqualTo("abc-123");
        assertThat(body.ownerName()).isEqualTo("João");
        assertThat(body.active()).isTrue();
    }

    @Test
    void shouldFailValidationWhenOwnerNameIsBlank() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        OpenAccountRequest request = new OpenAccountRequest("", AccountType.CHECKING);

        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        boolean hasOwnerMessage = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ownerName")
                        && v.getMessage().contains("Owner name is required"));
        assertThat(hasOwnerMessage).isTrue();
    }
}