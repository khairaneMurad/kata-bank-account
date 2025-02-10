package com.bank.kata.katabankaccount.client.dtos;

import com.bank.kata.katabankaccount.core.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String address;
    private List<Account> accountIds;
}
