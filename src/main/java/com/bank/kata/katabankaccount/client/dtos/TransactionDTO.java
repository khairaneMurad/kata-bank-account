package com.bank.kata.katabankaccount.client.dtos;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


public record TransactionDTO(Long id, String type, BigDecimal amount, String description, ZonedDateTime transactionTime, Long accountId, Long clientId, BigDecimal balanceAfterTransaction) {
    public TransactionDTO(String type, BigDecimal amount, String description, ZonedDateTime transactionTime) {
        this(null, type, amount, description, transactionTime, null, null, null);
    }
}
