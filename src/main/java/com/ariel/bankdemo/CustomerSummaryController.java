package com.ariel.bankdemo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ariel.bankdemo.bank.BankService;
import com.ariel.bankdemo.bank.CustomerSummary;
import com.ariel.bankdemo.customer.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer/{customerId}/summary")
public class CustomerSummaryController {


    private final BankService bankService;


    @GetMapping
    @ResponseBody
    CustomerSummary getCustomerSummary(@PathVariable("customerId") Long customerId) {
        try {
            return bankService.getSummaryForCustomer(customerId);
        } catch (CustomerNotFoundException e) {
            log.warn("Customer not found error", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
        }
    }











}
