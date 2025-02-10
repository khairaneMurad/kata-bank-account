package com.bank.kata.katabankaccount.infrastructure.gatwaysimpl;

import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.gateways.ClientGateway;
import com.bank.kata.katabankaccount.infrastructure.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ClientGatewayImpl implements ClientGateway {

    private final ClientRepository clientRepository;

    @Override
    public Client createOrUpdate(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> search(Long clientId) {
        return clientRepository.findById(clientId);
    }
}
