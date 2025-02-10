package com.bank.kata.katabankaccount.core.valueobjects;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Setter
@Getter
public final class Amount {
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final int DEFAULT_SCALE = 2;
    private final BigDecimal amount;

    public Amount(BigDecimal amount) {
        this.amount = amount;
    }

    public static Amount of(BigDecimal amount) {
        return new Amount(amount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public static Amount zero() {
        return new Amount(BigDecimal.ZERO);
    }

    public Amount add(Amount other) {
        return new Amount(this.amount.add(other.amount));
    }

    public Amount subtract(Amount other) {
        return new Amount(this.amount.subtract(other.amount));
    }

    public boolean isGreaterThan(Amount other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Amount other) {
        return this.amount.compareTo(other.amount) < 0;
    }

    public BigDecimal amount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Amount) obj;
        return Objects.equals(this.amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "Amount[" +
                "amount=" + amount + ']';
    }

}
