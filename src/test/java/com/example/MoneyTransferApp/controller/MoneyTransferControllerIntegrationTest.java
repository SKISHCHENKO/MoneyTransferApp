package com.example.MoneyTransferApp.controller;

import com.example.MoneyTransferApp.response.MoneyTransferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyTransferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMoneyTransferSuccess() throws Exception {
        String transferRequest = """
                    {
                        "cardFromNumber": "1234567890123456",
                        "cardToNumber": "6543210987654321",
                        "cardFromValidTill": "12/25",
                        "cardFromCVV": "123",
                        "amount": {
                            "value": 100000,
                            "currency": "RUB"
                        }
                    }
                """;

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").isNotEmpty());
    }

    @Test
    void testMoneyTransferValidationFailure() throws Exception {
        String invalidTransferRequest = """
                    {
                        "cardFromNumber": "1234567890123456",
                        "cardToNumber": "1234567890123456",
                        "cardFromValidTill": "12/25",
                        "cardFromCVV": "123",
                        "amount": {
                            "value": 100000,
                            "currency": "RUB"
                        }
                    }
                """;

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTransferRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Перевод на ту же самую карту невозможен!"));
    }

    @Test
    void testConfirmOperationSuccess() throws Exception {
        // Предварительно создать операцию перевода
        String transferRequest = """
                    {
                        "cardFromNumber": "1234567890123456",
                        "cardToNumber": "6543210987654321",
                        "cardFromValidTill": "12/25",
                        "cardFromCVV": "123",
                        "amount": {
                            "value": 100000,
                            "currency": "RUB"
                        }
                    }
                """;

        String response = mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Извлечь operationId из ответа
        String operationId = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(response, MoneyTransferResponse.class)
                .getOperationId();

        // Подтвердить операцию
        String confirmRequest = String.format("""
                    {
                        "operationId": "%s",
                        "code": "0000"
                    }
                """, operationId);

        mockMvc.perform(post("/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(confirmRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value(operationId));
    }

    @Test
    void testConfirmOperationInvalidCode() throws Exception {
        String confirmRequest = """
                    {
                        "operationId": "1",
                        "code": "1234"
                    }
                """;

        mockMvc.perform(post("/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(confirmRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Код не подходит!"));
    }
}