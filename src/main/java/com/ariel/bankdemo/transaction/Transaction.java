package com.ariel.bankdemo.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.ariel.bankdemo.account.Account;

import lombok.Builder;
import lombok.Getter;

/**
 * A transaction executed in one specific account. It can be a debit or credit depending on the amount.
 */
@Entity
@Getter
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    // Account for which the application is applied
    @ManyToOne
    private Account account;

    // Amount of the transaction. Positive for credit, negative for debit
    @Column(nullable = false)
    private BigDecimal amount;

    // UTC date time
    @Column(nullable = false)
    private LocalDateTime executionTime;

    @Builder
    public Transaction(final Long id, final Account account, final BigDecimal amount, final LocalDateTime executionTime) {
        this.id = id;
        this.account = account;
        this.amount = amount;
        this.executionTime = executionTime;
    }

    public Transaction() {
    }
}
