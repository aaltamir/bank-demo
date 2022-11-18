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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ariel.bankdemo.bank.BankService;
import com.ariel.bankdemo.customer.CustomerNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller in charge of creating the accounts for customers
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer/{customerId}/account")
public class AccountCreationController {

    private final BankService bankService;

    @Operation(summary = "Creates an account for one specific customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account Created for the specified customer",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found for specified id",
                    content = @Content)
    })
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    Long createAccountForCustomer(
            @Parameter(description = "The customer identifier") @PathVariable("customerId") Long customerId,
            @Parameter(description = "Additional attributes for account creation") @Valid @RequestBody AccountCreationRequest request
    ) {
        try {
            return bankService.createNewAccountForCustomer(customerId, request.initialCredit).getId();
        } catch (CustomerNotFoundException e) {
            log.warn("Customer not found error", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
        }
    }

    record AccountCreationRequest(@DecimalMin("0") BigDecimal initialCredit) {}
}
