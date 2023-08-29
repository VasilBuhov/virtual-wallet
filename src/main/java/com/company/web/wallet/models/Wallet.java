package com.company.web.wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    @Column(name = "balance")
    private BigDecimal balance;
    @JsonIgnore
    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private Set<Transaction> transactions;
    @Column(name = "overdraft_enabled")
    private int overdraftEnabled;
    @Transient
    private Currency currency;
    @Column(name = "status_deleted")
    private int statusDeleted;
    @Column(name = "interest_rate")
    private double interestRate;
    @Column(name = "number_of_wallet")
    private int numberOfWallet;

    public Wallet() {
    }

    public Wallet(int id, User owner, BigDecimal balance, Currency currency, int numberOfWallet) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.overdraftEnabled = 0;
        this.currency = currency;
        this.transactions = new HashSet<>();
        this.statusDeleted = 0;
        this.numberOfWallet = numberOfWallet;
    }

    public int getStatusDeleted() {
        return statusDeleted;
    }

    public void setStatusDeleted(int statusDeleted) {
        this.statusDeleted = statusDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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

    public int isOverdraftEnabled() {
        return overdraftEnabled;
    }

    public void setOverdraftEnabled(int overdraftEnabled) {
        this.overdraftEnabled = overdraftEnabled;
    }

    public int getOverdraftEnabled() {
        return overdraftEnabled;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getNumberOfWallet() {
        return numberOfWallet;
    }

    public void setNumberOfWallet(int numberOfWallet) {
        this.numberOfWallet = numberOfWallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id == wallet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
