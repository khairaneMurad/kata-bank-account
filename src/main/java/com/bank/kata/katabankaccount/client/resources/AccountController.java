package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.client.dtos.AccountDTO;
import com.bank.kata.katabankaccount.client.dtos.AccountStatementDTO;
import com.bank.kata.katabankaccount.client.dtos.TransactionDTO;
import com.bank.kata.katabankaccount.client.mappers.AccountMapper;
import com.bank.kata.katabankaccount.client.mappers.AccountStatementMapper;
import com.bank.kata.katabankaccount.client.mappers.TransactionMapper;
import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Transaction;
import com.bank.kata.katabankaccount.core.exceptions.DataNotFoundException;
import com.bank.kata.katabankaccount.core.services.AccountService;
import com.bank.kata.katabankaccount.core.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final AccountStatementMapper accountStatementMapper;


    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @RequestBody AccountDTO accountDTO) throws DataNotFoundException {
        Account account = accountMapper.toAccount(accountDTO);
        AccountDTO createdAccount = accountMapper.toAccountResponse(
                accountService.createAccount(accountDTO.clientId(), account)
        );
        return new ResponseEntity<>(createdAccount, CREATED);
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(
            @PathVariable Long accountId,
            @RequestBody TransactionDTO transactionDTO) throws DataNotFoundException {
        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        TransactionDTO createdTransaction = transactionMapper.toTransactionResponse(
                transactionService.createTransaction(accountId, transaction)
        );
        return new ResponseEntity<>(createdTransaction, CREATED);
    }

    @GetMapping("/{accountId}/statements")
    public ResponseEntity<List<AccountStatementDTO>> getAccountStatement(@PathVariable Long accountId) {
        List<AccountStatementDTO> statement = accountService.printAccountStatement(accountId)
                        .stream().map(accountStatementMapper::toResponse)
                        .toList();
        return ResponseEntity.ok(statement);
    }
}
