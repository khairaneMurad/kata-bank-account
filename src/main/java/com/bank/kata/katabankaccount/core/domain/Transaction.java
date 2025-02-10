package com.bank.kata.katabankaccount.core.domain;

import com.bank.kata.katabankaccount.core.enums.TransactionType;
import com.bank.kata.katabankaccount.core.valueobjects.Amount;
import com.bank.kata.katabankaccount.infrastructure.converters.AmountConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Builder
@Getter
@Setter
@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "transaction_type")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TransactionType type;

    private String description;

    @Convert(converter = AmountConverter.class)
    @Column(precision = 19, scale = 2)
    private Amount amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
