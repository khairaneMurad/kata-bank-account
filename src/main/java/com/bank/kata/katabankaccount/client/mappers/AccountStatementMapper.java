package com.bank.kata.katabankaccount.client.mappers;

import com.bank.kata.katabankaccount.client.dtos.AccountStatementDTO;
import com.bank.kata.katabankaccount.client.dtos.TransactionLightDTO;
import com.bank.kata.katabankaccount.core.domain.AccountStatement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountStatementMapper {

    public AccountStatementDTO toResponse(List<AccountStatement> statements) {
        if (statements == null || statements.isEmpty()) {
            return null;
        }

        return new AccountStatementDTO(
                getFirstName(statements),
                getLastName(statements),
                getAccountType(statements),
                getBalance(statements),
                prepareTransactions(statements)
        );
    }

    private String getFirstName(List<AccountStatement> statements) {
        return statements != null && !statements.isEmpty() ? statements.getFirst().firstName() : null;
    }

    private String getLastName(List<AccountStatement> statements) {
        return statements != null && !statements.isEmpty() ? statements.getFirst().lastName() : null;
    }

    private String getAccountType(List<AccountStatement> statements) {
        return statements != null && !statements.isEmpty() ? statements.getFirst().accountType() : null;
    }

    private BigDecimal getBalance(List<AccountStatement> statements) {
        return statements != null && !statements.isEmpty() ? statements.getFirst().balance().value() : null;
    }

    private List<TransactionLightDTO> prepareTransactions(List<AccountStatement> statements) {
        return statements.stream().map(statement-> new TransactionLightDTO(
                statement.transactionAmount() != null ? statement.transactionAmount().value() : null,
                statement.transactionType(),
                statement.transactionTime(),
                statement.description()
        )).toList();
    }
}
