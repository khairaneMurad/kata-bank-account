package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.TestContainersConfig;
import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClientControllerIntegrationTest extends TestContainersConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_createClientSuccessfully() throws Exception {
        // Given
        ClientDTO payload = new ClientDTO("toto", "toto", 36, "32 fake one");

        String requestBody = objectMapper.writeValueAsString(payload);

        // When
        MvcResult result = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(payload.firstName()))
                .andExpect(jsonPath("$.lastName").value(payload.lastName()))
                .andExpect(jsonPath("$.age").value(payload.age()))
                .andExpect(jsonPath("$.address").value(payload.address()))
                .andReturn();

        // Then
        ClientDTO createdClient = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ClientDTO.class
        );

        assertNotNull(createdClient.id());
        assertThat(createdClient)
                .extracting("firstName", "lastName", "age", "address")
                .containsExactly(
                        payload.firstName(), payload.lastName(), payload.age(), payload.address()
                );
        assertThat(createdClient.id()).isNotNull();
        assertThat(createdClient.accountIds()).isNull();
    }
}
