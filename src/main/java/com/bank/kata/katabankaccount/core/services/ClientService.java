package com.bank.kata.katabankaccount.core.services;

import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ClientService {

    private final ClientGateway clientGateway;

    public Client createClient(Client client) {
        return clientGateway.createOrUpdate(client);
    }
}

