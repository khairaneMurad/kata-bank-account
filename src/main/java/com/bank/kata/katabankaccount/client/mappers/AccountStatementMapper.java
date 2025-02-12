package com.bank.kata.katabankaccount.client.mappers;

import com.bank.kata.katabankaccount.client.dtos.AccountStatementDTO;
import com.bank.kata.katabankaccount.core.domain.AccountStatement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountStatementMapper {
    AccountStatementDTO toResponse(AccountStatement source);
}
