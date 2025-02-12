package com.bank.kata.katabankaccount.factories;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionFactory {
    public static Transaction createTransaction(Account account, TransactionType type, String description, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setAmount(Amount.of(amount));
        transaction.setTransactionTime(ZonedDateTime.now());
        transaction.setAccount(account);
        return transaction;
    }
}
