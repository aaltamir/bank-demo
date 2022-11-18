package com.ariel.bankdemo.transaction;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ariel.bankdemo.TimeService;
import com.ariel.bankdemo.account.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TimeService timeService;

    @Transactional
    public Transaction createTransactionForAccount(final Account account, final BigDecimal amount) {
        return transactionRepository.save(Transaction.builder()
                        .account(account)
                        .amount(amount)
                        .executionTime(timeService.nowUtc())
                .build());
    }
}
