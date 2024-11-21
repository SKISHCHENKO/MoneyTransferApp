package com.example.MoneyTransferApp.repository;

import com.example.MoneyTransferApp.request.ConfirmOperationRequest;
import com.example.MoneyTransferApp.request.MoneyTransferRequest;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MoneyTransferRepository {
    private final ConcurrentHashMap<String, MoneyTransferRequest> allTransfersMap = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();
    private final ConcurrentHashMap<String, String> allCodesMap = new ConcurrentHashMap<>();

    public void putCode(String id, String code) {
        allCodesMap.put(id, code);
    }

    public String getCode(String id) {
        return allCodesMap.get(id);
    }

    public String transfer(MoneyTransferRequest request) {
        id.incrementAndGet();
        allTransfersMap.put(id.toString(), request);
        return id.toString();
    }

    public String confirmOperation(ConfirmOperationRequest request) {
        String operationId = request.getOperationId();
        if (allTransfersMap.containsKey(operationId)) {
            return operationId;
        } else {
            return null;
        }
    }
}