package com.ariel.bankdemo;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ariel.bankdemo.bank.BankService;
import com.ariel.bankdemo.customer.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer/{customerId}/account")
public class AccountCreationController {

    private final BankService bankService;

    @PostMapping
    @ResponseBody
    Long createAccountForCustomer(@PathVariable("customerId") Long customerId, @Valid @RequestBody AccountCreationRequest request) {
        try {
            return bankService.createNewAccountForCustomer(customerId, request.initialCredit).getId();
        } catch (CustomerNotFoundException e) {
            log.warn("Customer not found error", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
        }
    }

    record AccountCreationRequest(@DecimalMin("0") BigDecimal initialCredit) {}
}
