package com.example.MoneyTransferApp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ConfirmOperationRequest {
    @NotBlank(message = "Код транзакции не заполнен!")
    @Pattern(regexp = "^[0-9]*$", message = "Код транзакции не подходит!")
    private String operationId;

    @NotBlank(message = "Код не подходит!")
    private String code;
}