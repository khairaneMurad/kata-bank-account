package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.TransactionGateway;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;

    public Transaction createTransaction(Long accountId, Transaction transaction) throws DataNotFoundException {
        log.info("Account validation on going");
        Account account = accountGateway.search(accountId)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
        log.info("Starting {} transaction for issuer acount {}", transaction.getType().name(), accountId);
        Amount newAccountBalance = transaction.getType().getOperation().apply(account.getBalance(), transaction.getAmount());
        transaction.setAccount(account);
        account.setBalance(newAccountBalance);

        log.info("Attaching the current client transaction to the issuer account number {}", accountId);
        accountGateway.createOrUpdate(account);
        return transactionGateway.createOrUpdate(transaction);
    }
}

