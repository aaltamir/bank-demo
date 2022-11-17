package com.ariel.bankdemo.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final long CUSTOMER_ID = 10L;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private Customer existingCustomer;

    @Test
    void findCustomerById() throws CustomerNotFoundException {
        when(customerRepository.findById(any())).thenReturn(Optional.of(existingCustomer));

        final Customer customer = customerService.findCustomerById(CUSTOMER_ID);

        verify(customerRepository).findById(CUSTOMER_ID);

        assertThat(customer).isSameAs(existingCustomer);
    }

    @Test
    void findCustomerByIdFailsWhenNotFound() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findCustomerById(CUSTOMER_ID))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}