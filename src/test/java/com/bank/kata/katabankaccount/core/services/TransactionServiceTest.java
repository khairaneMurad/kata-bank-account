package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.exceptions.InsufficientBalanceException;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.TransactionGateway;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void should_createTransactionSuccessfully_when_accountExists() throws DataNotFoundException {
        // Arrange
        Long accountId = 1L;
        Amount initialBalance = new Amount(new BigDecimal("1000.00"));
        Amount transactionAmount = new Amount(new BigDecimal("100.00"));

        Account account = Account.builder()
                .id(accountId)
                .balance(initialBalance)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(transactionAmount)
                .type(TransactionType.DEPOSIT)
                .build();

        Amount expectedNewBalance = new Amount(new BigDecimal("1100.00"));

        when(accountGateway.search(accountId)).thenReturn(Optional.of(account));
        when(accountGateway.createOrUpdate(any(Account.class))).thenReturn(account);
        when(transactionGateway.createOrUpdate(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction result = transactionService.createTransaction(accountId, transaction);

        // Assert
        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(expectedNewBalance, account.getBalance());

        verify(accountGateway).search(accountId);
        verify(accountGateway).createOrUpdate(account);
        verify(transactionGateway).createOrUpdate(transaction);
    }

    @Test
    void should_failTransactionCreation_when_accountBalanceIsInsufficient() {
        // Arrange
        Long accountId = 1L;
        Amount initialBalance = Amount.zero();
        Amount transactionAmount = new Amount(new BigDecimal("100.00"));

        Account account = Account.builder()
                .id(accountId)
                .balance(initialBalance)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(transactionAmount)
                .type(TransactionType.WITHDRAW)
                .build();

        when(accountGateway.search(accountId)).thenReturn(Optional.of(account));

        // Act
        assertThrows(InsufficientBalanceException.class, () ->
                transactionService.createTransaction(accountId, transaction)
        );

        // Assert
        verify(accountGateway).search(accountId);
        verify(accountGateway, never()).createOrUpdate(account);
        verify(transactionGateway, never()).createOrUpdate(transaction);
    }

    @Test
    void should_throwAccountNotFoundException_when_clientHasNoAccount() {
        // Arrange
        Long accountId = 1L;
        Transaction transaction = new Transaction();

        when(accountGateway.search(accountId)).thenReturn(Optional.empty());

        // Act + assert
        assertThrows(DataNotFoundException.class, () ->
                transactionService.createTransaction(accountId, transaction)
        );

        verify(accountGateway).search(accountId);
        verify(accountGateway, never()).createOrUpdate(any());
        verify(transactionGateway, never()).createOrUpdate(any());
    }
}
