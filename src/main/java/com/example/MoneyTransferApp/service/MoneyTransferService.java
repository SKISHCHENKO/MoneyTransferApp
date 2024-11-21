package com.example.MoneyTransferApp.service;
import com.example.MoneyTransferApp.exception.InputDataException;
import com.example.MoneyTransferApp.request.ConfirmOperationRequest;
import com.example.MoneyTransferApp.repository.MoneyTransferRepository;
import com.example.MoneyTransferApp.request.MoneyTransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class MoneyTransferService {
    private final MoneyTransferRepository repository;

    public MoneyTransferService(MoneyTransferRepository repository) {
        this.repository = repository;
    }

    public String transfer(MoneyTransferRequest request) {
        if (request.getCardFromNumber().equals(request.getCardToNumber())) {
            throw new InputDataException("Перевод на ту же самую карту невозможен!");
        }
        String id = repository.transfer(request);
        repository.putCode(id, "0000");

        // Логирование информации о транзакции
        log.info("Транзакция: ID={}, карта отправителя={}, карта получателя={}, сумма={} {}, код подтверждения={}",
                id,
                request.getCardFromNumber(),
                request.getCardToNumber(),
                request.getAmount().getValue()/100,
                request.getAmount().getCurrency(),
                "0000"
        );

        return id;
    }

    public String confirmOperation(ConfirmOperationRequest request) {
        if (request == null) {
            throw new InputDataException("Запрос пуст");
        }

        String operationId = request.getOperationId();
        if (request.getCode().equals(repository.getCode(operationId))) {
            String confirmedId = repository.confirmOperation(request);
            // Логирование успешного подтверждения
            log.info("Операция подтверждена: ID={}, код={}", operationId, request.getCode());


            return confirmedId;
        } else {
            throw new InputDataException("Код не подходит!");
        }
    }
}