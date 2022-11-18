package com.ariel.bankdemo.customer;

/**
 * Used when the customer is not found for the specified id.
 */
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(final Long customerId) {
        super("Customer not found for id: " + customerId);
    }
}
