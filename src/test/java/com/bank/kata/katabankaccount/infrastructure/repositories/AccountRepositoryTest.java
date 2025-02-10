package com.bank.kata.katabankaccount.infrastructure.repositories;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class AccountRepositoryTest extends TestContainersConfig{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldCreateAccountForClient() {
        // Given
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setAge(30);
        client.setAddress("123 Main St");
        Client savedClient = clientRepository.save(client);

        Account account = new Account();
        account.setType("SAVINGS");
        account.setBalance(Amount.of(new BigDecimal("1000")));
        account.setClient(savedClient);

        // When
        Account savedAccount = accountRepository.save(account);

        // Then
        assertNotNull(savedAccount.getId());
        assertEquals("SAVINGS", savedAccount.getType());
        assertEquals(Amount.of(new BigDecimal("1000")), savedAccount.getBalance());
        assertEquals(savedClient.getId(), savedAccount.getClient().getId());
    }



    // TODO: THIS SHOULD be a service level testing and business logic
    /*@Test
    @Disabled
    void testInvalidTransactionScenarios() {
        Client client = clientRepository.save(createTestClient());
        Account account = accountRepository.save(createTestAccount(client, BigDecimal.valueOf(1000.0)));

        // Test negative balance prevention
        assertThrows(InsufficientFundsException.class, () -> {
            transactionGateway.createOrUpdate(createTransaction(account, TransactionType.WITHDRAW, "Large withdrawal", BigDecimal.valueOf(-2000.0)));
        });

        // Verify account balance remained unchanged
        Account unchangedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertEquals(BigDecimal.valueOf(1000.0), unchangedAccount.getBalance());
    }*/
}
