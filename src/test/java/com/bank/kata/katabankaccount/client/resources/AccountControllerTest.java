package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.client.dtos.AccountDTO;
import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.bank.kata.katabankaccount.client.dtos.TransactionDTO;
import com.bank.kata.katabankaccount.client.mappers.AccountMapper;
import com.bank.kata.katabankaccount.client.mappers.ClientMapper;
import com.bank.kata.katabankaccount.client.mappers.TransactionMapper;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.bank.kata.katabankaccount.core.services.AccountService;
import com.bank.kata.katabankaccount.core.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest extends TestContainersConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientGateway clientGateway;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    void shouldAddTransactionToAndAccount() throws Exception{
        // Given
        // create client
        Client createdClient = getCreatedClient();

        // create account + associate it to a client
        Account createdAccount = getCreatedAccount(createdClient);

        // start transaction
        TransactionDTO payload = TransactionDTO.builder()
                .amount(new BigDecimal(100))
                .type("DEPOSIT")
                .description("init deposit transaction")
                .build();

        String requestBody = objectMapper.writeValueAsString(payload);

        // When
        BigDecimal expectedTotalBalance = new BigDecimal("1100.00");
        MvcResult result = mockMvc.perform(post("/accounts/" + createdAccount.getId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientId").value(createdClient.getId()))
                .andExpect(jsonPath("$.accountId").value(createdAccount.getId()))
                .andExpect(jsonPath("$.amount").value(payload.getAmount()))
                .andExpect(jsonPath("$.type").value(payload.getType()))
                .andReturn();

        // Then
        TransactionDTO createdTransaction = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionDTO.class);

        assertNotNull(createdTransaction.getId());
        assertThat(createdTransaction)
                .extracting("amount", "type", "clientId", "AccountId", "balanceAfterTransaction")
                .containsExactly(
                        payload.getAmount(), payload.getType(), createdClient.getId(), createdAccount.getId(), expectedTotalBalance
                );
        assertThat(createdTransaction.getId()).isNotNull();
    }

    private Account getCreatedAccount(Client createdClient) {
        AccountDTO accountDTO = AccountDTO.builder()
                .balance(1000)
                .type("COMPTE COURANT")
                .build();

        return accountService.createAccount(createdClient.getId(), accountMapper.toAccount(accountDTO));
    }

    private Client getCreatedClient() {
        ClientDTO client = ClientDTO.builder()
                .firstName("toto")
                .lastName("toto")
                .age(36)
                .address("32 fake one")
                .build();

        return clientGateway.createOrUpdate(clientMapper.toClient(client));
    }

    @Test
    void getAccountStatement() throws Exception {
        // create client
        Client createdClient = getCreatedClient();

        // create account + associate it to a client
        Account createdAccount = getCreatedAccount(createdClient);

        // create multiple transaction
        TransactionDTO t1 = TransactionDTO.builder()
                .amount(new BigDecimal("100.0"))
                .type("DEPOSIT")
                .description("init deposit transaction")
                .build();

        TransactionDTO t2 = TransactionDTO.builder()
                .amount(new BigDecimal("100.0"))
                .type("WITHDRAW")
                .description("ATM WITHDRAW")
                .build();

        transactionService.createTransaction(createdAccount.getId(), transactionMapper.toTransaction(t1));
        transactionService.createTransaction(createdAccount.getId(), transactionMapper.toTransaction(t2));

        BigDecimal expectedTotalBalance = new BigDecimal("1000.0");
        mockMvc.perform(get("/accounts/{accountId}/statement", createdAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(createdClient.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(createdClient.getLastName()))
                .andExpect(jsonPath("$[0].balance.amount").value(expectedTotalBalance))
                .andExpect(jsonPath("$[0].amount.amount").value(t1.getAmount()))
                .andExpect(jsonPath("$[0].transactionType").value(t1.getType()))
                .andExpect(jsonPath("$[0].description").value(t1.getDescription()))

                .andExpect(jsonPath("$[1].firstName").value(createdClient.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(createdClient.getLastName()))
                .andExpect(jsonPath("$[1].balance.amount").value(expectedTotalBalance))
                .andExpect(jsonPath("$[1].amount.amount").value(t2.getAmount()))
                .andExpect(jsonPath("$[1].transactionType").value(t2.getType()))
                .andExpect(jsonPath("$[1].description").value(t2.getDescription()))
                .andReturn();
    }
}