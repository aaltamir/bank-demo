package com.ariel.bankdemo.customer;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Main customer related service.
 */
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Find a customer by its id.
     * @param customerId The customer id.
     * @return The customer
     * @throws CustomerNotFoundException In case the customer with the specified id doesn't exist.
     */
    public Customer findCustomerById(final Long customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
