package com.ariel.bankdemo.account;

import java.math.BigDecimal;

// Issued when not enough funds are found to do a debit in the account.
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(final BigDecimal amount) {
        super("Insufficient funds to process the following amount: " + amount);
    }
}
