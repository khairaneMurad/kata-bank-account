package com.bank.kata.katabankaccount.infrastructure.gatwaysimpl;

import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.gateways.TransactionGateway;
import com.bank.kata.katabankaccount.infrastructure.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TransactionGatewayImpl implements TransactionGateway {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createOrUpdate(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> searchByAccountId(Long ids) {
        return transactionRepository.findAllByAccountId(ids);
    }
}
