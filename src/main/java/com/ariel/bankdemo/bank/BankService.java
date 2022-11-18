package com.ariel.bankdemo.bank;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ariel.bankdemo.account.Account;
import com.ariel.bankdemo.account.AccountService;
import com.ariel.bankdemo.customer.Customer;
import com.ariel.bankdemo.customer.CustomerNotFoundException;
import com.ariel.bankdemo.customer.CustomerService;
import com.ariel.bankdemo.transaction.TransactionService;

import lombok.RequiredArgsConstructor;

/**
 * Main service in charge of provide the main functionalities of the application.
 */
@Service
@RequiredArgsConstructor
public class BankService {

    private final CustomerService customerService;

    private final AccountService accountService;

    private final TransactionService transactionService;

    /**
     * This creates a new bank account for the customer specified by its id.
     * @param customerId The customer id for which the account is created
     * @param initialCredit The positive initial credit.
     * @return The created account
     * @throws CustomerNotFoundException When the customer is not found
     */
    @Transactional
    public Account createNewAccountForCustomer(final Long customerId, final BigDecimal initialCredit)
            throws CustomerNotFoundException {

        if(initialCredit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid Initial credit: " + initialCredit);
        }

        final Customer customer = customerService.findCustomerById(customerId);

        final Account account = accountService.createAccountForCustomer(customer, initialCredit);

        if(!initialCredit.equals(BigDecimal.ZERO)) {
            transactionService.createTransactionForAccount(account, initialCredit);
        }

        return account;
    }

    /**
     * Gets a summary for a customer composed by the customer information, accounts information and transactions
     * per account.
     * @param customerId The id of the customer for which we want the summary
     * @return The customer summary
     * @throws CustomerNotFoundException If the customer is not found.
     */
    @Transactional(readOnly = true)
    public CustomerSummary getSummaryForCustomer(final Long customerId) throws CustomerNotFoundException {

        final Customer customer = customerService.findCustomerById(customerId);

        return new CustomerSummary(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                getAccountsSummaryForCustomer(customer)
        );
    }

    private List<CustomerSummary.AccountSummary> getAccountsSummaryForCustomer(final Customer customer) {
        return accountService.getAccountsForCustomer(customer)
                .stream().map(account -> new CustomerSummary.AccountSummary(
                        account.getId(),
                        account.getBalance(),
                        getTransactionsSummaryForAccount(account)
                )).toList();
    }

    private List<CustomerSummary.TransactionSumary> getTransactionsSummaryForAccount(final Account account) {
        return transactionService.findTransactionsForAccount(account)
                .stream().map(transaction -> new CustomerSummary.TransactionSumary(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getExecutionTime()
                )).toList();
    }


}
