package com.ariel.bankdemo.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ariel.bankdemo.customer.Customer;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("10.00");

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private Customer testCustomer;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    void createAccountForCustomer() {
        final Account savedAccount = getTestAccount();

        when(accountRepository.save(any())).thenReturn(savedAccount);

        final Account createdAccount = accountService.createAccountForCustomer(testCustomer, INITIAL_BALANCE);

        verify(accountRepository).save(accountArgumentCaptor.capture());

        assertThat(accountArgumentCaptor.getValue())
                .isNotNull()
                .returns(testCustomer, Account::getCustomer)
                .returns(INITIAL_BALANCE, Account::getBalance);

        assertThat(createdAccount).isSameAs(savedAccount);
    }

    @Test
    void updateBalance() throws InsufficientFundsException {
        final Account oneAccount = getTestAccount();

        final Account updatedAccount = accountService.updateBalance(oneAccount, new BigDecimal("15"));

        assertThat(updatedAccount).isNotNull().returns(new BigDecimal("25"), Account::getBalance);
    }

    @Test
    void updateBalanceFailsWhenNotEnoughMoney() {
        final Account oneAccount = getTestAccount();

        assertThatThrownBy(() -> accountService.updateBalance(oneAccount, new BigDecimal("-25")))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void getAccountForBalanceUpdate() {
        final Account oneAccount = getTestAccount();
        when(accountRepository.findByIdIs(anyLong())).thenReturn(Optional.of(oneAccount));

        final Account account = accountService.getAccountForBalanceUpdate(10L).orElseThrow();

        verify(accountRepository).findByIdIs(10L);

        assertThat(account).isSameAs(oneAccount);
    }

    private Account getTestAccount() {
        return Account.builder()
                .initialBalance(new BigDecimal("10"))
                .customer(testCustomer)
                .build();
    }
}