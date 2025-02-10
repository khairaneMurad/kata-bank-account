package com.bank.kata.katabankaccount.infrastructure.repositories;

import com.bank.kata.katabankaccount.core.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccountId(Long accountId);
}
