package com.bank.kata.katabankaccount.client.dtos;

import java.math.BigDecimal;
import java.util.List;

public record AccountStatementDTO(
    String firstName,
    String lastName,
    String accountType,
    BigDecimal balance,
    List<TransactionLightDTO> transactions
){}

