package com.ariel.bankdemo.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ariel.bankdemo.account.Account;
import com.ariel.bankdemo.account.AccountService;
import com.ariel.bankdemo.customer.Customer;
import com.ariel.bankdemo.customer.CustomerNotFoundException;
import com.ariel.bankdemo.customer.CustomerService;
import com.ariel.bankdemo.transaction.Transaction;
import com.ariel.bankdemo.transaction.TransactionService;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {
    private static final BigDecimal INITIAL_CREDIT = new BigDecimal("10");
    private static final String NAME = "Name";
    private static final String SURNAME = "Surname";
    private static final long CUSTOMER_ID = 10L;
    private static final long ACCOUNT_ID = 20L;
    private static final long TRANSACTION_ID = 30L;
    private static final LocalDateTime EXECUTION_TIME = LocalDateTime.of(2022, 10, 10, 11, 22);

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private BankService bankService;

    @Mock
    private Customer testCustomer;

    @Mock
    private Account testAccount;

    @Test
    void createNewAccountForCustomer() throws CustomerNotFoundException {
        when(customerService.findCustomerById(anyLong())).thenReturn(testCustomer);
        when(accountService.createAccountForCustomer(any(), any())).thenReturn(testAccount);

        final Account createdAccount =
                bankService.createNewAccountForCustomer(CUSTOMER_ID, INITIAL_CREDIT);

        verify(customerService).findCustomerById(CUSTOMER_ID);
        verify(accountService).createAccountForCustomer(testCustomer, INITIAL_CREDIT);
        verify(transactionService).createTransactionForAccount(testAccount, INITIAL_CREDIT);

        assertThat(createdAccount).isSameAs(testAccount);
    }

    @Test
    void createNewAccountForCustomerDoNotCreateTransaction() throws CustomerNotFoundException {
        when(customerService.findCustomerById(anyLong())).thenReturn(testCustomer);
        when(accountService.createAccountForCustomer(any(), any())).thenReturn(testAccount);

        final Account createdAccount =
                bankService.createNewAccountForCustomer(CUSTOMER_ID, BigDecimal.ZERO);

        verify(accountService).createAccountForCustomer(testCustomer, BigDecimal.ZERO);
        verifyNoInteractions(transactionService);
    }

    @Test
    void createNewAccountForCustomerFailsWithNegativeAmount() {
        assertThatThrownBy(() -> bankService.createNewAccountForCustomer(CUSTOMER_ID, new BigDecimal("-10")))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void testGetSummaryForCustomer() throws CustomerNotFoundException {
        final Customer customer = Customer.builder()
                .id(CUSTOMER_ID)
                .name(NAME)
                .surname(SURNAME)
                .build();

        final Account account = Account.builder()
                .id(ACCOUNT_ID)
                .customer(customer)
                .initialBalance(INITIAL_CREDIT)
                .build();

        final Transaction transaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .amount(INITIAL_CREDIT)
                .account(account)
                .executionTime(EXECUTION_TIME)
                .build();

        when(customerService.findCustomerById(anyLong())).thenReturn(customer);
        when(accountService.getAccountsForCustomer(any())).thenReturn(Collections.singletonList(account));
        when(transactionService.findTransactionsForAccount(any())).thenReturn(Collections.singletonList(transaction));

        final CustomerSummary customerSummary = bankService.getSummaryForCustomer(CUSTOMER_ID);

        verify(customerService).findCustomerById(CUSTOMER_ID);
        verify(accountService).getAccountsForCustomer(customer);
        verify(transactionService).findTransactionsForAccount(account);

        assertThat(customerSummary).isNotNull()
                .returns(CUSTOMER_ID, CustomerSummary::customerId)
                .returns(NAME, CustomerSummary::name)
                .returns(SURNAME, CustomerSummary::surname);

        assertThat(customerSummary.accounts())
                .hasSize(1)
                .singleElement()
                .returns(ACCOUNT_ID, CustomerSummary.AccountSummary::accountId)
                .returns(INITIAL_CREDIT, CustomerSummary.AccountSummary::balance);

        assertThat(customerSummary.accounts().get(0).transactions())
                .hasSize(1)
                .singleElement()
                .returns(TRANSACTION_ID, CustomerSummary.TransactionSumary::transactionId)
                .returns(INITIAL_CREDIT, CustomerSummary.TransactionSumary::amount)
                .returns(EXECUTION_TIME, CustomerSummary.TransactionSumary::when);
    }
}