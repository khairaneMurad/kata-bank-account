package com.bank.kata.katabankaccount.client.dtos;

import com.bank.kata.katabankaccount.core.domain.Transaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class AccountDTO {
    private Long id;
    private String type;
    private double balance;
    private Long clientId;
    private List<Transaction> transactionIds; // Just store the ID instead of whole client object
}
