package com.bank.kata.katabankaccount.client.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private String type;
    private BigDecimal amount;
    private String description;
    private Long accountId;
    private Long clientId;
    private BigDecimal balanceAfterTransaction;
}
