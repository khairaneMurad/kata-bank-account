package com.bank.kata.katabankaccount.client.dtos;

import com.bank.kata.katabankaccount.core.domain.Transaction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public record AccountDTO(Long id, String type, BigDecimal balance, Long clientId, List<Transaction> transactionIds){
    public AccountDTO(String type, BigDecimal balance, Long clientId) {
        this(null, type, balance, clientId, Collections.emptyList());
    }
}
