package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.client.dtos.AccountDTO;
import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.bank.kata.katabankaccount.client.mappers.AccountMapper;
import com.bank.kata.katabankaccount.client.mappers.ClientMapper;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.services.AccountService;
import com.bank.kata.katabankaccount.core.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final AccountService accountService;

    private final ClientMapper clientMapper;
    private final AccountMapper accountMapper;

    @PostMapping("/client")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        clientDTO.setId(null);
        Client client = clientMapper.toClient(clientDTO);
        ClientDTO createdClient = clientMapper.toClientResponse(
                clientService.createClient(client)
        );
        return new ResponseEntity<>(createdClient, CREATED);
    }

    @PostMapping("{clientId}/account")
    public ResponseEntity<AccountDTO> createAccount(
            @PathVariable Long clientId,
            @RequestBody AccountDTO accountDTO) {
        accountDTO.setId(null);
        Account account = accountMapper.toAccount(accountDTO);
        AccountDTO createdAccount = accountMapper.toAccountResponse(
                accountService.createAccount(clientId, account)
        );
        return new ResponseEntity<>(createdAccount, CREATED);
    }
}
