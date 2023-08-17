package com.company.web.wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
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
    private boolean overdraftEnabled;
    private Currency currency;

    public Wallet() {
    }

    public Wallet(int id, User owner, BigDecimal balance, boolean overdraftEnabled, Currency currency, Set<Transaction> transactions) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.overdraftEnabled = overdraftEnabled;
        this.currency = currency;
        this.transactions = transactions;
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

    public boolean isOverdraftEnabled() {
        return overdraftEnabled;
    }

    public void setOverdraftEnabled(boolean overdraftEnabled) {
        this.overdraftEnabled = overdraftEnabled;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
