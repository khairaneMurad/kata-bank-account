package com.bank.kata.katabankaccount.core.domain;

import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;

import java.time.ZonedDateTime;

public record AccountStatement(
    String firstName,
    String lastName,
    String accountType,
    Amount balance,
    Amount transactionAmount,
    TransactionType transactionType,
    ZonedDateTime transactionTime,
    String description
){}
