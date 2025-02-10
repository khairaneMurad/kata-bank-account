package com.bank.kata.katabankaccount.client.mappers;

import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AmountMapper {
    default BigDecimal toBigDecimal(Amount amount) {
        return amount != null ? amount.getAmount() : null;
    }

    default Amount toAmount(BigDecimal value) {
        return value != null ? new Amount(value) : null;
    }
}
