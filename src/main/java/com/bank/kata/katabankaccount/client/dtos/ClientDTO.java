package com.bank.kata.katabankaccount.client.dtos;

import com.bank.kata.katabankaccount.core.domain.Account;

import java.util.List;

public record ClientDTO (Long id, String firstName, String lastName, int age, String address, List<Account> accountIds){
    public ClientDTO(String firstName, String lastName, int age, String address) {
        this(null, firstName, lastName, age, address, List.of());
    }
}
