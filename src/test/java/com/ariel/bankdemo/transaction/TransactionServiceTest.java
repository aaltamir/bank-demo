package com.ariel.bankdemo.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ariel.bankdemo.TimeService;
import com.ariel.bankdemo.account.Account;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final LocalDateTime NOW_UTC = LocalDateTime.of(2022, 11, 20, 10, 22);
    private static final BigDecimal AMOUNT = new BigDecimal("10");

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TimeService timeService;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private Account testAccount;

    @Mock
    private Transaction aTransaction;

    @Captor
    private ArgumentCaptor<Transaction> transactionArgumentCaptor;

    @Test
    void createTransactionForAccount() {
        when(transactionRepository.save(any())).thenReturn(aTransaction);
        when(timeService.nowUtc()).thenReturn(NOW_UTC);

        final Transaction createdTransaction =
                transactionService.createTransactionForAccount(testAccount, AMOUNT);

        verify(transactionRepository).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .isNotNull()
                .returns(testAccount, Transaction::getAccount)
                .returns(AMOUNT, Transaction::getAmount)
                .returns(NOW_UTC, Transaction::getExecutionTime);

        assertThat(createdTransaction).isSameAs(aTransaction);
    }

    @Test
    void getTransactionsForAccount() {
        when(transactionRepository.findAllByAccount(any())).thenReturn(Collections.singletonList(aTransaction));

        final List<Transaction> transactions = transactionService.findTransactionsForAccount(testAccount);

        verify(transactionRepository).findAllByAccount(testAccount);

        assertThat(transactions).containsExactly(aTransaction);
    }
}