package com.ariel.bankdemo.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class ExistingCustomerInitializerTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    ExistingCustomerInitializer customerInitializer;

    @Mock
    private Customer savedCustomer;

    @Test
    void testCustomersCreated() {
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        customerInitializer.run();
        verify(customerRepository, times(100)).save(any());
    }
}