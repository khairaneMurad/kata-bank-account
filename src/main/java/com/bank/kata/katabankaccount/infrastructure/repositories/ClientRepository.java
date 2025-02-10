package com.bank.kata.katabankaccount.infrastructure.repositories;

import com.bank.kata.katabankaccount.core.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
