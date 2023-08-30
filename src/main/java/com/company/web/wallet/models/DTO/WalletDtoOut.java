package com.company.web.wallet.models.DTO;

import com.company.web.wallet.models.Transaction;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

public class WalletDtoOut {
    @NotNull
    private String owner;
    @NotNull
    private BigDecimal balance;
    private Set<Transaction> transactions;
    @NotNull
    private int overdraftEnabled;

    public WalletDtoOut() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getOverdraftEnabled() {
        return overdraftEnabled;
    }

    public void setOverdraftEnabled(int overdraftEnabled) {
        this.overdraftEnabled = overdraftEnabled;
    }
}
