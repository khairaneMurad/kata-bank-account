package com.bank.kata.katabankaccount.client.mappers;

import com.bank.kata.katabankaccount.client.dtos.ClientDTO;
import com.bank.kata.katabankaccount.core.domain.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(source = "accounts", target = "accountIds")
    ClientDTO toClientResponse(Client source);

    @Mapping(target = "accounts", ignore = true)
    Client toClient(ClientDTO source);
}
