package com.bank.kata.katabankaccount.client.dtos;

import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;

import java.time.ZonedDateTime;

public record AccountStatementDTO(
    String firstName,
    String lastName,
    String accountType,
    Amount balance,
    Amount amount,
    TransactionType transactionType,
    ZonedDateTime transactionTime,
    String description
){}
