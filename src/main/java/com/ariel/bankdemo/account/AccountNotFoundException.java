package com.ariel.bankdemo.account;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(final Long id) {
        super("Account not found with id: " + id);
    }
}
