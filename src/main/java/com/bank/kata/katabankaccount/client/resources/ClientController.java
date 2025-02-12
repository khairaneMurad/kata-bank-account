package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.bank.kata.katabankaccount.client.mappers.ClientMapper;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        Client client = clientMapper.toClient(clientDTO);
        ClientDTO createdClient = clientMapper.toClientResponse(
                clientService.createClient(client)
        );
        return new ResponseEntity<>(createdClient, CREATED);
    }
}
