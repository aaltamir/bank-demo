package com.ariel.bankdemo.account;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.ariel.bankdemo.customer.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // We use this lock to prevent concurrency problems when updating this account.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByIdIs(Long id);

    List<Account> findAllByCustomer(Customer customer);
}
