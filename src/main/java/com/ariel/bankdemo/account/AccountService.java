package com.ariel.bankdemo.account;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ariel.bankdemo.customer.Customer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccountForCustomer(final Customer customer, final BigDecimal initialBalance) {
        final var newAccount = Account.builder()
                .customer(customer)
                .initialBalance(initialBalance)
                .build();
        return accountRepository.save(newAccount);
    }

    @Transactional
    public Account updateBalance(final Account account, final BigDecimal amount) throws InsufficientFundsException {
        final BigDecimal updatedAmount = account.getBalance().add(amount);

        if(updatedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(amount);
        }

        account.setBalance(updatedAmount);
        return account;
    }

    @Transactional
    public Optional<Account> getAccountForBalanceUpdate(final Long accountId) {
        return accountRepository.findByIdIs(accountId);
    }

}
