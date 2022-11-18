package com.ariel.bankdemo.customer;

import java.util.Locale;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Just a simple runner to setup initial customers in the application.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class ExistingCustomerInitializer implements CommandLineRunner {

    public static final int NUMBER_OF_CUSTOMERS = 100;
    private final CustomerRepository customerRepository;

    @Override
    public void run(final String... args) {

        final Faker faker = new Faker(Locale.ENGLISH);

        IntStream.range(0, NUMBER_OF_CUSTOMERS).forEach(ignored -> {
            final var customer = Customer.builder()
                    .name(faker.name().firstName())
                    .surname(faker.name().lastName())
                    .build();

            final Customer savedCustomer =  customerRepository.save(customer);

            log.info("Customer created with id={}, name={}, surname={}", savedCustomer.getId(), savedCustomer.getName(),
                    savedCustomer.getSurname());
        });
    }
}
