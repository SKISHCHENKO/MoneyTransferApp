package com.example.MoneyTransferApp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MoneyTransferContainerTest {

    private static final int APP_PORT = 5500;

    private static GenericContainer<?> appContainer;
    private static RestTemplate restTemplate;

    @BeforeAll
    static void setUp() {
        // Инициализация Docker-контейнера
        appContainer = new GenericContainer<>(DockerImageName.parse("money-transfer-app:latest"))
                .withExposedPorts(APP_PORT);
        appContainer.start();
        restTemplate = new RestTemplate();
    }

    @Test
    void testMoneyTransferSuccess() {
        // Получение URL из контейнера
        String baseUrl = "http://" + appContainer.getHost() + ":" + appContainer.getMappedPort(APP_PORT);

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

        // Создание заголовков запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Формирование HTTP-запроса
        HttpEntity<String> requestEntity = new HttpEntity<>(transferRequest, headers);

        // Выполнение POST-запроса
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/transfer", requestEntity, String.class);

        // Проверка результата
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("operationId");
    }
}