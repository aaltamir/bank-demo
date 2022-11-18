package com.ariel.bankdemo.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ariel.bankdemo.TimeService;
import com.ariel.bankdemo.account.Account;

import lombok.RequiredArgsConstructor;

/**
 * Service in charge of the transactions.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TimeService timeService;

    /**
     * Creates a new transaction for one specified account
     * @param account The account for which we create the transaction
     * @param amount The amount of the transaction
     * @return The created transaction
     */
    @Transactional
    public Transaction createTransactionForAccount(final Account account, final BigDecimal amount) {
        return transactionRepository.save(Transaction.builder()
                        .account(account)
                        .amount(amount)
                        .executionTime(timeService.nowUtc())
                .build());
    }

    /**
     * Returns all the transactions for one specified bank account.
     * @param account The account for which we want to retrieve the transactions
     * @return The list of transactions. It can be empty.
     */
    @Transactional
    public List<Transaction> findTransactionsForAccount(final Account account) {
        return transactionRepository.findAllByAccount(account);
    }

}
