package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.AccountStatement;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {

    private final ClientGateway clientGateway;
    private final AccountGateway accountGateway;

    public Account createAccount(Long clientId, Account account) throws DataNotFoundException {
        Client client = clientGateway.search(clientId)
                .orElseThrow(() -> new DataNotFoundException("Client not found"));
        account.setClient(client);
        return accountGateway.createOrUpdate(account);
    }

    public List<AccountStatement> printAccountStatement(Long accountId) {
        List<AccountStatement> statements = accountGateway.getAccountStatement(accountId);

        if (statements.isEmpty()) {
            log.info("No statement found for account ID: {}", accountId);
            return Collections.emptyList();
        }

        AccountStatement firstStatement = statements.getFirst();
        log.info("Account Statement");
        log.info("----------------");
        log.info("Client: {} {}", firstStatement.firstName(), firstStatement.lastName());
        log.info("Account Type: {}", firstStatement.accountType());
        log.info("Current Balance: {}", firstStatement.balance());
        log.info("\nTransactions:");
        log.info("----------------");

        statements.forEach(statement -> {
            if (statement.transactionAmount() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                log.info("Date: {}, Amount: {}, Type: {}, Description: {}",
                        statement.transactionTime().format(formatter.withZone(ZoneId.of("Europe/Paris"))),
                        statement.transactionAmount().value(),
                        statement.transactionType(),
                        statement.description());
            }
        });

        return statements;
    }
}

