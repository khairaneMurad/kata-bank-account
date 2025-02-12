package com.bank.kata.katabankaccount.infrastructure.gatwaysimpl;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.AccountStatement;
import com.bank.kata.katabankaccount.core.gateways.AccountGateway;
import com.bank.kata.katabankaccount.infrastructure.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AccountGatewayImpl implements AccountGateway {

    private final AccountRepository accountRepository;

    @Override
    public Account createOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> search(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public List<AccountStatement> getAccountStatement(Long accountId) {
        return accountRepository.fetchAccountStatement(accountId);
    }
}
