package com.ariel.bankdemo.account;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ariel.bankdemo.customer.Customer;

import lombok.RequiredArgsConstructor;

/**
 * Service class in charge of Account operations
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Creates one account for one specific customer, including its initial balance
     * @param customer The customer for which the account will be created
     * @param initialBalance The initial balance
     * @return The new account
     */
    @Transactional
    public Account createAccountForCustomer(final Customer customer, final BigDecimal initialBalance) {
        final var newAccount = Account.builder()
                .customer(customer)
                .initialBalance(initialBalance)
                .build();
        return accountRepository.save(newAccount);
    }

    /**
     * Updates the balance of one account adding the specified value to it. This method fails if there is no enough
     * balance in case of a debit (negative amount)
     * @param account The account to which the amount will be applied.
     * @param amount The amount. This can be negative
     * @return The updated account
     * @throws InsufficientFundsException When there is no funds in the balance in case of debit operations.
     */
    @Transactional
    public Account updateBalance(final Account account, final BigDecimal amount) throws InsufficientFundsException {
        final BigDecimal updatedAmount = account.getBalance().add(amount);

        if(updatedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(amount);
        }

        account.setBalance(updatedAmount);
        return account;
    }

    /**
     * Used to get the account in order of being updated later. This lock the account to avoid concurrent updates.
     * @param accountId The account identifier.
     * @return The account to be updated.
     * @throws AccountNotFoundException When the account is not found for the specified id.
     */
    @Transactional
    public Account getAccountForBalanceUpdate(final Long accountId) throws AccountNotFoundException {
        return accountRepository.findByIdIs(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

}
