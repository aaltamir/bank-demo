package com.ariel.bankdemo.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;

/**
 * A customer in our Demo Bank.
 */
@Entity
@Getter
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Builder
    public Customer(final String name, final String surname) {
        this.name = name;
        this.surname = surname;
    }

    protected Customer() {
    }
}
