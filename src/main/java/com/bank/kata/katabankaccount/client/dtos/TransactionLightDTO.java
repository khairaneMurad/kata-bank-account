package com.bank.kata.katabankaccount.client.dtos;

import com.bank.kata.katabankaccount.core.enums.TransactionType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TransactionLightDTO(
        BigDecimal transactionAmount,
        TransactionType transactionType,
        ZonedDateTime transactionTime,
        String description
) { }
