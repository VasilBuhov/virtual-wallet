package com.company.web.wallet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
@Entity
@Table(name = "interest_rates")
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "interest")
    private double interest;
    @Column(name = "date_of_entry")
    private LocalDate changedOnDate;

    public InterestRate() {
    }

    public InterestRate(int id, double interest, LocalDate changedOnDate) {
        this.id = id;
        this.interest = interest;
        this.changedOnDate = changedOnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public LocalDate getChangedOnDate() {
        return changedOnDate;
    }

    public void setChangedOnDate(LocalDate changedOnDate) {
        this.changedOnDate = changedOnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterestRate that = (InterestRate) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
