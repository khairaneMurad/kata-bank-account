package com.bank.kata.katabankaccount.core.enums;

import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import lombok.Getter;

import java.util.function.BiFunction;

@Getter
public enum TransactionType {
    DEPOSIT(Amount::add),
    WITHDRAW(Amount::subtract);

    private final BiFunction<Amount, Amount, Amount> operation;

    TransactionType(BiFunction<Amount, Amount, Amount> operation) {
        this.operation = operation;
    }
}
