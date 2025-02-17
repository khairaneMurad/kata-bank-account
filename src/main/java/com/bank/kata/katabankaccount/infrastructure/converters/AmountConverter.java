package com.bank.kata.katabankaccount.infrastructure.converters;

import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class AmountConverter implements AttributeConverter<Amount, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Amount amount) {
        return amount == null ? null : amount.value();
    }

    @Override
    public Amount convertToEntityAttribute(BigDecimal value) {
        return value == null ? null : Amount.of(value);
    }
}
