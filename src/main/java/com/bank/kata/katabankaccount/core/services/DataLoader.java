package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.annotations.KataDataLoader;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


@KataDataLoader
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ClientGateway clientGateway;
    private final AccountGateway accountGateway;
    private final TransactionService transactionService;

    @Override
    public void run(String... args) throws DataNotFoundException {
        // Create and save client
        Client client = Client.builder()
                .firstName("TOTO")
                .lastName("TOTO")
                .age(30)
                .address("45 titi toto")
                .build();
        clientGateway.createOrUpdate(client);

        // Create and save account
        Account account = Account.builder()
                .type("SAVINGS")
                .balance(Amount.of(new BigDecimal("1000")))
                .client(client)
                .build();
        Account updated = accountGateway.createOrUpdate(account);
        Long accountId = updated.getId();

        // Create and save deposit transaction
        Transaction deposit = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .description("Initial deposit")
                .amount(Amount.of(BigDecimal.valueOf(500)))
                .account(account)
                .transactionTime(ZonedDateTime.now())
                .build();

        transactionService.createTransaction(accountId, deposit);

        // Create and save withdrawal transaction
        Transaction withdrawal = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .description("ATM withdrawal")
                .amount(Amount.of(BigDecimal.valueOf(200)))
                .account(account)
                .transactionTime(ZonedDateTime.now())
                .build();
        transactionService.createTransaction(accountId, withdrawal);
    }
}

