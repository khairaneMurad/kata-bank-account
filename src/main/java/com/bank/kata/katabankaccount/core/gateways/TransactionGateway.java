package com.bank.kata.katabankaccount.core.gateways;

import com.bank.kata.katabankaccount.core.domain.Transaction;

import java.util.List;

public interface TransactionGateway {
    Transaction createOrUpdate(Transaction transaction);
    List<Transaction> searchByAccountId(Long ids);
}
