package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.exceptions.InsufficientBalanceException;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.bank.kata.katabankaccount.core.gateways.TransactionGateway;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import com.bank.kata.katabankaccount.factories.AccountFactory;
import com.bank.kata.katabankaccount.factories.TransactionFactory;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static com.bank.kata.katabankaccount.core.enums.TransactionType.DEPOSIT;
import static com.bank.kata.katabankaccount.core.enums.TransactionType.WITHDRAW;
import static com.bank.kata.katabankaccount.factories.AccountFactory.createTestAccount;
import static com.bank.kata.katabankaccount.factories.ClientFactory.createTestClient;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
class TransactionServiceIntegrationTest extends TestContainersConfig{

    @Autowired private ClientGateway clientGateway;
    @Autowired private TransactionGateway transactionGateway;
    @Autowired private AccountGateway accountGateway;
    @Autowired private AccountService accountService;
    @Autowired private TransactionService sut;

    Client client;

    @BeforeEach
    void setUp() {
        client = createTestClient("toto", "toto", 31, "FAKE ADDRESS");
    }


    @Test
    void should_addTransactionAndUpdateAccountBalance_whenMakingADeposit() throws DataNotFoundException {
        // Create a client
        Client savedClient = clientGateway.createOrUpdate(client);
        assertNotNull(savedClient.getId());

        // Create an account for the client
        Account account = AccountFactory.createTestAccount(savedClient, new BigDecimal("1000.0"));
        Account savedAccount = accountService.createAccount(client.getId(), account);

        assertNotNull(savedAccount.getId());
        assertEquals(Amount.of(BigDecimal.valueOf(1000.00)), savedAccount.getBalance());

        // Create a transaction
        Transaction transaction = TransactionFactory.createTransaction(savedAccount, TransactionType.DEPOSIT, "Initial deposit", BigDecimal.valueOf(500.0));

        Transaction savedTransaction = sut.createTransaction(savedAccount.getId(), transaction);

        assertNotNull(savedTransaction.getId());
        assertEquals(Amount.of(BigDecimal.valueOf(500.00)), savedTransaction.getAmount());

        // Verify account balance update
        Account updatedAccount = accountGateway.search(savedAccount.getId()).orElseThrow();
        assertEquals(Amount.of(BigDecimal.valueOf(1500.00)), updatedAccount.getBalance());

        // Verify transaction list
        List<Transaction> transactions = transactionGateway.searchByAccountId(savedAccount.getId());
        assertEquals(1, transactions.size());
        assertEquals(TransactionType.DEPOSIT, transactions.getFirst().getType());
    }

    @Test
    void should_changeAccountBalance_when_MakingMultipleTypeOfTransactions() throws DataNotFoundException, InsufficientBalanceException {
        // Create client and account
        Client client = clientGateway.createOrUpdate(createTestClient("toto", "toto", 31, "FAKE ADDRESS"));
        Account account = accountService.createAccount(client.getId(), createTestAccount(client, BigDecimal.valueOf(2000)));

        // Perform multiple transactions
        Long accountId = account.getId();

        sut.createTransaction(
                accountId,
                TransactionFactory.createTransaction(
                        account, WITHDRAW, "ATM Withdrawal", BigDecimal.valueOf(500)
                )
        );
        sut.createTransaction(
                accountId,
                TransactionFactory.createTransaction(
                        account, TransactionType.DEPOSIT, "Salary", BigDecimal.valueOf(3000)
                )
        );
        sut.createTransaction(
                accountId,
                TransactionFactory.createTransaction(
                        account, WITHDRAW, "Online Payment", BigDecimal.valueOf(200)
                )
        );

        // Verify global balance
        Account updatedAccount = accountGateway.search(accountId).orElseThrow();
        assertEquals(account, updatedAccount);
        assertEquals(Amount.of(new BigDecimal("4300.00")), updatedAccount.getBalance());

        // Verify transaction history
        List<Transaction> transactions = transactionGateway.searchByAccountId(accountId);
        Assertions.assertThat(transactions)
                .hasSize(3)
                .extracting("type", "amount")
                .containsExactlyInAnyOrderElementsOf(
                        List.of(
                                tuple(WITHDRAW, Amount.of(BigDecimal.valueOf(500.00))),
                                tuple(DEPOSIT, Amount.of(BigDecimal.valueOf(3000.00))),
                                tuple(WITHDRAW, Amount.of(BigDecimal.valueOf(200.00)))
                        )
                );
    }
}