package com.example.MoneyTransferApp.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MoneyTransferRequest {
    @NotBlank(message = "Номер карты отправителя обязателен!")
    @Size(min = 16, max = 16, message = "Номер карты отправителя должен содержать 16 цифр!")
    @Pattern(regexp = "\\d+")
    private String cardFromNumber;

    @NotBlank(message = "Номер карты получателя обязателен!")
    @Size(min = 16, max = 16, message = "Номер карты получателя должен содержать 16 цифр!")
    @Pattern(regexp = "\\d+")
    private String cardToNumber;

    @NotBlank(message = "Срок действия карты отправителя обязателен!")
    @Size(min = 5, max = 5, message = "Срок действия карты отправителя обязателен!")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$")
    private String cardFromValidTill;

    @NotBlank(message = "CVC / CVC2 номер карты отправителя обязателен!")
    @Size(min = 3, max = 3, message = "CVC / CVC2 номер карты отправителя обязателен!")
    private String cardFromCVV;

    private Money amount;
    @Data
    @AllArgsConstructor
    public static class Money {
        @NotBlank(message = "Сумма перевода обязательна!")
        @Min(value = 1L, message = "Сумма перевода должна быть положительным числом!")
        private Long value;

        @NotBlank(message = "Не указана валюта перевода!")
        private String currency;
    }
}