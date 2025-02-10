package com.bank.kata.katabankaccount.core.gateways;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.AccountStatementDTO;

import java.util.List;
import java.util.Optional;

public interface AccountGateway {
    Account createOrUpdate(Account account);
    Optional<Account> search(Long accountId);
    List<AccountStatementDTO> getAccountStatement(Long accountId);
}
