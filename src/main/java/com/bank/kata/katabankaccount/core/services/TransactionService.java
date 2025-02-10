package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.TransactionGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.bank.kata.katabankaccount.core.enums.TransactionType.DEPOSIT;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;

    public Transaction createTransaction(Long accountId, Transaction transaction) {
        log.info("Account validation on going");
        Account account = accountGateway.search(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        log.info("Starting {} transaction for issuer acount {}", transaction.getType().name(), accountId);
        transaction.setAccount(account);
        if (DEPOSIT.equals(transaction.getType())) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        log.info("Attaching the current client transaction to the issuer account number {}", accountId);
        accountGateway.createOrUpdate(account);
        return transactionGateway.createOrUpdate(transaction);
    }
}

