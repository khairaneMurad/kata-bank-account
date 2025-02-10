package com.bank.kata.katabankaccount.core.gateways;

import com.bank.kata.katabankaccount.core.domain.Client;

import java.util.Optional;

public interface ClientGateway {
    Client createOrUpdate(Client client);
    Optional<Client> search(Long clientId);
}
