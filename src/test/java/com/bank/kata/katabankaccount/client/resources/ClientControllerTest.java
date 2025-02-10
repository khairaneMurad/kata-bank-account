package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.client.dtos.AccountDTO;
import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.bank.kata.katabankaccount.client.mappers.AccountMapper;
import com.bank.kata.katabankaccount.client.mappers.ClientMapper;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClientControllerTest extends TestContainersConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientGateway clientGateway;

    @Test
    @DisplayName("Should create a new client successfully")
    void shouldCreateClientSuccessfully() throws Exception {
        // Given
        ClientDTO payload = ClientDTO.builder()
                .firstName("toto")
                .lastName("toto")
                .age(36)
                .address("32 fake one")
                .build();

        String requestBody = objectMapper.writeValueAsString(payload);

        // When
        MvcResult result = mockMvc.perform(post("/clients/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(payload.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(payload.getLastName()))
                .andExpect(jsonPath("$.age").value(payload.getAge()))
                .andExpect(jsonPath("$.address").value(payload.getAddress()))
                .andReturn();

        // Then
        ClientDTO createdClient = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ClientDTO.class
        );

        assertNotNull(createdClient.getId());
        assertThat(createdClient)
                .extracting("firstName", "lastName", "age", "address")
                .containsExactly(
                        payload.getFirstName(), payload.getLastName(), payload.getAge(), payload.getAddress()
                );
        assertThat(createdClient.getId()).isNotNull();
        assertThat(createdClient.getAccountIds()).isNull();
    }

    @Test
    @DisplayName("Should create an account and associate it to a client successfully")
    void should_CreateAndAssociateNewAccountToAPreviouslyCreatedClientSuccessfully() throws Exception {
        // Given
        ClientDTO client = ClientDTO.builder()
                .firstName("toto")
                .lastName("toto")
                .age(36)
                .address("32 fake one")
                .build();

        Client createdClient = clientGateway.createOrUpdate(clientMapper.toClient(client));

        AccountDTO payload = AccountDTO.builder()
                .balance(1000)
                .type("COMPTE COURANT")
                .build();

        String requestBody = objectMapper.writeValueAsString(payload);

        // When
        MvcResult result = mockMvc.perform(post("/clients/" + createdClient.getId() + "/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientId").value(createdClient.getId()))
                .andExpect(jsonPath("$.balance").value(payload.getBalance()))
                .andExpect(jsonPath("$.type").value(payload.getType()))
                .andReturn();

        // Then
        AccountDTO createdAccount = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AccountDTO.class
        );

        assertNotNull(createdAccount.getId());
        assertThat(createdAccount)
                .extracting("balance", "type", "clientId")
                .containsExactly(
                        payload.getBalance(), payload.getType(), createdClient.getId()
                );
        assertThat(createdAccount.getId()).isNotNull();
        assertThat(createdAccount.getTransactionIds()).isNull();
    }
}
