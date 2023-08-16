package com.company.web.wallet.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "card_number")
    private double cardNumber;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "card_holder")
    private User cardHolder;
    @Column(name = "check_number")
    private int checkNumber;
    @Column(name = "status_deleted")
    private int statusDeleted;
    @JsonIgnore
    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER)
    private Set<Transaction> transactionsSet;

    public Card() {
    }

    public Card(int id, double cardNumber, LocalDate expirationDate, User cardHolder, int checkNumber, int statusDeleted) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cardHolder = cardHolder;
        this.checkNumber = checkNumber;
        this.transactionsSet = new HashSet<>();
        this.statusDeleted = statusDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(double cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(User cardHolder) {
        this.cardHolder = cardHolder;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Set<Transaction> getTransactionsSet() {
        return transactionsSet;
    }

    public void setTransactionsSet(Set<Transaction> transactionsSet) {
        this.transactionsSet = transactionsSet;
    }

    public int getStatusDeleted() {
        return statusDeleted;
    }

    public void setStatusDeleted(int statusDeleted) {
        this.statusDeleted = statusDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
