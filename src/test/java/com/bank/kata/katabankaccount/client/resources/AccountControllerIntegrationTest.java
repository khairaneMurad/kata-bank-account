package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.client.dtos.AccountDTO;
import com.bank.kata.katabankaccount.client.dtos.AccountStatementDTO;
import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.bank.kata.katabankaccount.client.dtos.TransactionDTO;
import com.bank.kata.katabankaccount.client.mappers.AccountMapper;
import com.bank.kata.katabankaccount.client.mappers.ClientMapper;
import com.bank.kata.katabankaccount.client.mappers.TransactionMapper;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.bank.kata.katabankaccount.core.services.AccountService;
import com.bank.kata.katabankaccount.core.services.TransactionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerIntegrationTest extends TestContainersConfig {

    public static final String STATEMENTS_URI = "/accounts/{accountId}/statements";
    public static final String CREATE_TRANSACTION_URI = "/accounts/{accountId}/transactions";
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
    void should_createAndAssociateNewAccountToAPreviouslyCreatedClientSuccessfully() throws Exception {
        // Given
        ClientDTO client = new ClientDTO("toto", "toto", 36, "32 fake one");

        Client createdClient = clientGateway.createOrUpdate(clientMapper.toClient(client));

        AccountDTO payload = new AccountDTO("COMPTE COURANT", new BigDecimal(1000), createdClient.getId());

        String requestBody = objectMapper.writeValueAsString(payload);

        // When
        MvcResult result = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientId").value(createdClient.getId()))
                .andExpect(jsonPath("$.balance").value(payload.balance()))
                .andExpect(jsonPath("$.type").value(payload.type()))
                .andReturn();

        // Then
        AccountDTO createdAccount = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AccountDTO.class
        );

        assertNotNull(createdAccount.id());
        assertThat(createdAccount)
                .extracting("balance", "type", "clientId")
                .containsExactly(
                        payload.balance(), payload.type(), createdClient.getId()
                );
        assertThat(createdAccount.id()).isNotNull();
        assertThat(createdAccount.transactionIds()).isNull();
    }

    @Test
    void should_addTransactionToAnAccount() throws Exception {
        // Given
        // create client
        Client createdClient = getCreatedClient();

        // create account + associate it to a client
        Account createdAccount = getCreatedAccount(createdClient);

        // start transaction
        TransactionDTO payload = new TransactionDTO("DEPOSIT", new BigDecimal(100), "init deposit transaction", ZonedDateTime.now());

        String requestBody = objectMapper.writeValueAsString(payload);

        // When
        BigDecimal expectedTotalBalance = new BigDecimal(1100);
        MvcResult result = mockMvc.perform(post(CREATE_TRANSACTION_URI, createdAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientId").value(createdClient.getId()))
                .andExpect(jsonPath("$.accountId").value(createdAccount.getId()))
                .andExpect(jsonPath("$.amount").value(payload.amount()))
                .andExpect(jsonPath("$.type").value(payload.type()))
                .andReturn();

        // Then
        TransactionDTO createdTransaction = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionDTO.class);

        assertNotNull(createdTransaction.id());
        assertThat(createdTransaction)
                .extracting("amount", "type", "clientId", "accountId", "balanceAfterTransaction")
                .containsExactly(
                        payload.amount(), payload.type(), createdClient.getId(), createdAccount.getId(), expectedTotalBalance
                );
        assertThat(createdTransaction.id()).isNotNull();
    }

    @Test
    void should_fetchAccountStatementData() throws Exception {
        // create client
        Client createdClient = getCreatedClient();

        // create account + associate it to a client
        Account createdAccount = getCreatedAccount(createdClient);

        // create multiple transaction
        ZonedDateTime t1Time = ZonedDateTime.now();
        TransactionDTO t1 = new TransactionDTO("DEPOSIT", new BigDecimal("100.0"), "init deposit transaction", t1Time);
        ZonedDateTime t2Time = t1Time.minusDays(1);
        TransactionDTO t2 = new TransactionDTO("WITHDRAW", new BigDecimal("100.0"), "ATM WITHDRAW", t2Time);

        transactionService.createTransaction(createdAccount.getId(), transactionMapper.toTransaction(t1));
        transactionService.createTransaction(createdAccount.getId(), transactionMapper.toTransaction(t2));

        BigDecimal expectedTotalBalance = new BigDecimal("1000.0");
        mockMvc.perform(get(STATEMENTS_URI, createdAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(createdClient.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(createdClient.getLastName()))
                .andExpect(jsonPath("$[0].balance.amount").value(expectedTotalBalance))
                .andExpect(jsonPath("$[0].amount.amount").value(t1.amount()))
                .andExpect(jsonPath("$[0].transactionType").value(t1.type()))
                .andExpect(jsonPath("$[0].description").value(t1.description()))
                .andExpect(jsonPath("$[0].transactionTime").value(t1Time.toInstant().toString()))

                .andExpect(jsonPath("$[1].firstName").value(createdClient.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(createdClient.getLastName()))
                .andExpect(jsonPath("$[1].balance.amount").value(expectedTotalBalance))
                .andExpect(jsonPath("$[1].amount.amount").value(t2.amount()))
                .andExpect(jsonPath("$[1].transactionType").value(t2.type()))
                .andExpect(jsonPath("$[1].description").value(t2.description()))
                .andExpect(jsonPath("$[1].transactionTime").value(t2Time.toInstant().toString()));
    }

    @Test
    void should_returnAccountStatementWithEmptyTransactions_when_theClientHaveMadeZeroTransaction() throws Exception {
        Client createdClient = getCreatedClient();
        Account createdAccount = getCreatedAccount(createdClient);
        MvcResult result = mockMvc.perform(get(STATEMENTS_URI, createdAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<AccountStatementDTO> statement = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertThat(statement)
                .extracting("transactionType", "description", "amount", "transactionTime")
                .containsExactlyElementsOf(List.of(tuple(null, null, null, null)));
    }

    @Test
    void should_returnEmptyAccountStatement_when_missingAccount() throws Exception {
        MvcResult result = mockMvc.perform(get(STATEMENTS_URI, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<AccountStatementDTO> statement = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertThat(statement).isEmpty(); 
    }

    private Account getCreatedAccount(Client createdClient) throws DataNotFoundException {
        AccountDTO accountDTO = new AccountDTO("COMPTE COURANT", new BigDecimal(1000), createdClient.getId());
        return accountService.createAccount(createdClient.getId(), accountMapper.toAccount(accountDTO));
    }


    private Client getCreatedClient() {
        ClientDTO client = new ClientDTO("toto", "toto", 36, "32 fake one");
        new ClientDTO("toto", "toto", 36, "32 fake one");
        return clientGateway.createOrUpdate(clientMapper.toClient(client));
    }
}