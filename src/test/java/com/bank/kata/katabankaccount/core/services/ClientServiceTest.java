package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.bank.kata.katabankaccount.factories.ClientFactory.createTestClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientGateway clientGateway;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void should_createAclient() {
        // Arrange
        Client expectedClient = createTestClient("toto", "toto", 31, "FAKE ADDRESS");
        expectedClient.setId(1L);

        when(clientGateway.createOrUpdate(expectedClient)).thenReturn(expectedClient);

        // Act
        Client actualClient = clientService.createClient(expectedClient);

        // Assert
        assertNotNull(actualClient);
        assertEquals(expectedClient.getId(), actualClient.getId());
        assertEquals(expectedClient.getFirstName(), actualClient.getFirstName());
        assertEquals(expectedClient.getLastName(), actualClient.getLastName());
        assertEquals(expectedClient.getAge(), actualClient.getAge());
        assertEquals(expectedClient.getAddress(), actualClient.getAddress());
    }
}
