package com.bank.kata.katabankaccount.factories;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.Client;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;

import java.math.BigDecimal;

public class AccountFactory {
    public static Account createTestAccount(Client client, BigDecimal initialBalance) {
        Account account = new Account();
        account.setType("CHECKING");
        account.setBalance(Amount.of(initialBalance));
        account.setClient(client);
        return account;
    }
}
