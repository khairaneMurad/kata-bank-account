package com.bank.kata.katabankaccount.client.mappers;

import com.bank.kata.katabankaccount.client.dtos.AccountDTO;
import com.bank.kata.katabankaccount.core.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {TransactionMapper.class, ClientMapper.class}
)
public interface AccountMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "transactions", target = "transactionIds")
    @Mapping(source = "balance.amount", target = "balance")
    AccountDTO toAccountResponse(Account source);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(source = "balance", target = "balance.amount")
    Account toAccount(AccountDTO source);
}
