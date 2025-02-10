package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.AccountStatementDTO;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {

    private final ClientGateway clientGateway;
    private final AccountGateway accountGateway;

    public Account createAccount(Long clientId, Account account) {
        Client client = clientGateway.search(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        account.setClient(client);
        return accountGateway.createOrUpdate(account);
    }

    public List<AccountStatementDTO> printAccountStatement(Long accountId) {
        List<AccountStatementDTO> statements = accountGateway.getAccountStatement(accountId);

        if (statements.isEmpty()) {
            log.info("No statement found for account ID: {}", accountId);
            return Collections.emptyList();
        }

        AccountStatementDTO firstStatement = statements.getFirst();
        log.info("Account Statement");
        log.info("----------------");
        log.info("Client: {} {}", firstStatement.getFirstName(), firstStatement.getLastName());
        log.info("Account Type: {}", firstStatement.getAccountType());
        log.info("Current Balance: {}", firstStatement.getBalance());
        log.info("\nTransactions:");
        log.info("----------------");

        statements.forEach(statement -> {
            if (statement.getAmount() != null) {
                log.info("Amount: {}, Type: {}, Description: {}",
                        statement.getAmount(),
                        statement.getTransactionType(),
                        statement.getDescription());
            }
        });

        return statements;
    }
}

