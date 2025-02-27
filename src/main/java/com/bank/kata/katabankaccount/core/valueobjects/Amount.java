package com.bank.kata.katabankaccount.core.valueobjects;

import com.bank.kata.katabankaccount.core.exceptions.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public record Amount(BigDecimal value) {
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final int DEFAULT_SCALE = 2;

    public static Amount of(BigDecimal amount) {
        return new Amount(amount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public static Amount zero() {
        return new Amount(BigDecimal.ZERO);
    }

    public Amount add(Amount other) {
        return new Amount(this.value.add(other.value));
    }

    public Amount subtract(Amount other) {
        if (this.value.subtract(other.value).compareTo(Amount.zero().value) < 0) {
            log.info("Insufficient account balance");
            throw new InsufficientBalanceException("You don't have enough money to make this operation");
        } else {
            return new Amount(this.value.subtract(other.value));
        }
    }
}
