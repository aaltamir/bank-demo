package com.ariel.bankdemo.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CustomerSummary(Long customerId, String name, String surname, List<AccountSummary> accounts) {
    public record AccountSummary(Long accountId, BigDecimal balance, List<TransactionSumary> transactions) { }
    public record TransactionSumary(Long transactionId, BigDecimal amount, LocalDateTime when) { }
}
