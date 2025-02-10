package com.bank.kata.katabankaccount.core.domain;

import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import lombok.Data;

@Data
public class AccountStatementDTO {
    private String firstName;
    private String lastName;
    private String accountType;
    private Amount balance;
    private Amount amount;
    private TransactionType transactionType;
    private String description;

    public AccountStatementDTO(String firstName, String lastName, String accountType, Amount balance, Amount amount, TransactionType transactionType, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
        this.balance = balance;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
    }
}
