package com.bank.kata.katabankaccount.factories;

import com.bank.kata.katabankaccount.core.domain.Client;

public class ClientFactory {
     public static Client createTestClient(String firstName, String lastName, int age, String address) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setAge(age);
        client.setAddress(address);
        return client;
    }
}
