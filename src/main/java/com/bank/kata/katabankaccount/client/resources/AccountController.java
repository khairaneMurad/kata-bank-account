package com.bank.kata.katabankaccount.client.resources;

import com.bank.kata.katabankaccount.client.dtos.TransactionDTO;
import com.bank.kata.katabankaccount.client.mappers.TransactionMapper;
import com.bank.kata.katabankaccount.core.domain.AccountStatementDTO;
import com.bank.kata.katabankaccount.core.domain.Transaction;
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

    @PostMapping("/{accountId}/transaction")
    public ResponseEntity<TransactionDTO> createTransaction(
            @PathVariable Long accountId,
            @RequestBody TransactionDTO transactionDTO) {
        transactionDTO.setId(null);
        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        TransactionDTO createdTransaction = transactionMapper.toTransactionResponse(
                transactionService.createTransaction(accountId, transaction)
        );
        return new ResponseEntity<>(createdTransaction, CREATED);
    }

    @GetMapping("/{accountId}/statement")
    public ResponseEntity<List<AccountStatementDTO>> getAccountStatement(@PathVariable Long accountId) {
        List<AccountStatementDTO> statement = accountService.printAccountStatement(accountId);
        return ResponseEntity.ok(statement);
    }
}
