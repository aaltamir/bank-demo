package com.ariel.bankdemo.account;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.ariel.bankdemo.customer.Customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Simple Account for Customer
 */
@Entity
@Getter
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer customer;

    @Setter
    @Column(nullable = false)
    private BigDecimal balance;

    @Builder
    public Account(final Customer customer, final BigDecimal initialBalance) {
        this.customer = customer;
        this.balance = initialBalance;
    }

    protected Account() {
    }
}
