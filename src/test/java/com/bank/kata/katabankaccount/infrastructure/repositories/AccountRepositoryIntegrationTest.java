package com.bank.kata.katabankaccount.infrastructure.repositories;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class AccountRepositoryIntegrationTest extends TestContainersConfig{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void should_createAnAccountForClient() {
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
}
