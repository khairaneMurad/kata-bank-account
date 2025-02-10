package com.bank.kata.katabankaccount.client.mappers;

import com.bank.kata.katabankaccount.client.dtos.TransactionDTO;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, ClientMapper.class, AmountMapper.class})
public interface TransactionMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.client.id", target = "clientId")
    @Mapping(source = "account.balance.amount", target = "balanceAfterTransaction")
    TransactionDTO toTransactionResponse(Transaction source);

    @Mapping(target = "account", ignore = true)
    Transaction toTransaction(TransactionDTO source);
}
