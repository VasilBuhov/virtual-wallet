package com.company.web.wallet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Table(name = "savings_wallets")
public class SavingsWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    @Column(name = "deposit")
    private BigDecimal deposit;
    @Column(name = "duration_months")
    private int durationMonths;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @OneToOne
    @JoinColumn(name = "from_wallet")
    private Wallet fromWallet;
    @Column(name = "interest_rate")
    private double interestRate;
    @Column(name = "interest_reward")
    private BigDecimal interestReward;
    @Column(name = "is_deleted")
    private int isDeleted;

    public SavingsWallet() {
    }

    public SavingsWallet(int id, User owner, BigDecimal deposit, int durationMonths, LocalDateTime endDate, Wallet fromWallet, double interestRate) {
        this.id = id;
        this.owner = owner;
        this.deposit = deposit;
        this.durationMonths = durationMonths;
        this.endDate = endDate;
        this.fromWallet = fromWallet;
        this.interestRate = interestRate;
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

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Wallet getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(Wallet fromWallet) {
        this.fromWallet = fromWallet;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getInterestReward() {
        return interestReward;
    }

    public void setInterestReward(BigDecimal interestReward) {
        this.interestReward = interestReward;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavingsWallet that = (SavingsWallet) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
