package com.example.MoneyTransferApp;

import com.example.MoneyTransferApp.exception.InputDataException;
import com.example.MoneyTransferApp.request.MoneyTransferRequest;
import com.example.MoneyTransferApp.service.MoneyTransferService;
import com.example.MoneyTransferApp.repository.MoneyTransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MoneyTransferServiceTest {
	private MoneyTransferService service;
	private MoneyTransferRepository repository;

	@BeforeEach
	void setUp() {
		// Создаем мок для репозитория
		repository = mock(MoneyTransferRepository.class);
		// Инициализируем сервис с моком репозитория
		service = new MoneyTransferService(repository);
	}

	@Test
	void transfer_sameCard_shouldThrowException() {
		// Создаем запрос перевода с одинаковыми номерами карт
		MoneyTransferRequest request = new MoneyTransferRequest();
		request.setCardFromNumber("1234567812345678");
		request.setCardToNumber("1234567812345678");
		request.setCardFromValidTill("12/24");
		request.setCardFromCVV("123");
		request.setAmount(new MoneyTransferRequest.Money(100L, "RUB"));

		// Проверяем, что вызов метода transfer выбрасывает исключение
		InputDataException exception = assertThrows(InputDataException.class, () -> {
			service.transfer(request);
		});

		// Проверяем текст исключения
		assertEquals("Перевод на ту же самую карту невозможен!", exception.getMessage());
	}
}