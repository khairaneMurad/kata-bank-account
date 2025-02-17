package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.AccountStatement;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private ClientGateway clientGateway;

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private AccountService accountService;

    @Test
    void should_createAccountSuccessfully_when_clientExists() throws DataNotFoundException {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        Account account = new Account();
        account.setType("SAVINGS");
        account.setBalance(new Amount(new BigDecimal(1000)));

        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setClient(client);
        savedAccount.setType("SAVINGS");
        savedAccount.setBalance(new Amount(new BigDecimal("1000.50")));

        when(clientGateway.search(clientId)).thenReturn(Optional.of(client));
        when(accountGateway.createOrUpdate(account)).thenReturn(savedAccount);

        // Act
        Account result = accountService.createAccount(clientId, account);

        // Assert
        assertNotNull(result);
        assertEquals(client, result.getClient());
        assertEquals("SAVINGS", result.getType());
        assertEquals(new BigDecimal("1000.50"), result.getBalance().value());

        verify(clientGateway).search(clientId);
        verify(accountGateway).createOrUpdate(account);
    }

    @Test
    void should_throwDataNotFoundException_when_clientDoesNotExist() {
        // Arrange
        Long clientId = 1L;
        Account account = new Account();

        when(clientGateway.search(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> accountService.createAccount(clientId, account));

        verify(clientGateway).search(clientId);
        verify(accountGateway, never()).createOrUpdate(any());
    }

    @Test
    void should_printAndReturnAccountStatement_when_dataExists() {
        // Arrange
        Long accountId = 1L;
        Amount accountBalance = new Amount(new BigDecimal(1000));
        ZonedDateTime t1Time = ZonedDateTime.now();
        ZonedDateTime t2Time = t1Time.minusHours(1);
        List<AccountStatement> mockStatements = Arrays.asList(
                new AccountStatement("toto", "toto", "Savings", accountBalance, new Amount(new BigDecimal(500)), TransactionType.DEPOSIT, t1Time, "Initial deposit"),
                new AccountStatement("toto", "toto", "Savings", accountBalance, new Amount(new BigDecimal(200)), TransactionType.WITHDRAW, t2Time, "ATM withdrawal")
        );

        when(accountGateway.getAccountStatement(accountId)).thenReturn(mockStatements);

        // Act
        List<AccountStatement> result = accountService.printAccountStatement(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountGateway).getAccountStatement(accountId);
    }

    @Test
    void should_returnEmptyList_when_emptyStatements() {
        // Arrange
        Long accountId = 1L;
        when(accountGateway.getAccountStatement(accountId)).thenReturn(Collections.emptyList());

        // Act
        List<AccountStatement> result = accountService.printAccountStatement(accountId);

        // Assert
        assertTrue(result.isEmpty());
        verify(accountGateway).getAccountStatement(accountId);
    }


}
