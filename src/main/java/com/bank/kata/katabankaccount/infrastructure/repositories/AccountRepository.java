package com.bank.kata.katabankaccount.infrastructure.repositories;

import com.bank.kata.katabankaccount.core.domain.Account;
import com.bank.kata.katabankaccount.core.domain.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = """
            select new com.bank.kata.katabankaccount.core.domain.AccountStatement(c.firstName, c.lastName,
            ac.type, ac.balance, t.amount, t.type, t.transactionTime, t.description)
            from Client c join c.accounts ac left join ac.transactions t
            where ac.id = :accountId
            """)
    List<AccountStatement> fetchAccountStatement(@Param("accountId") Long accountId);
}
