package com.example.MoneyTransferApp.controller;

import com.example.MoneyTransferApp.request.ConfirmOperationRequest;
import com.example.MoneyTransferApp.request.MoneyTransferRequest;
import com.example.MoneyTransferApp.response.MoneyTransferResponse;
import com.example.MoneyTransferApp.service.MoneyTransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MoneyTransferController {
    private final MoneyTransferService service;

    public MoneyTransferController(MoneyTransferService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public ResponseEntity<MoneyTransferResponse> transfer(@Valid @RequestBody MoneyTransferRequest request) {
        MoneyTransferResponse response = new MoneyTransferResponse();
        response.setOperationId(service.transfer(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<MoneyTransferResponse> confirmOperation(@Valid @RequestBody ConfirmOperationRequest request) {
        MoneyTransferResponse response = new MoneyTransferResponse();
        response.setOperationId(service.confirmOperation(request));
        return ResponseEntity.ok(response);
    }
}