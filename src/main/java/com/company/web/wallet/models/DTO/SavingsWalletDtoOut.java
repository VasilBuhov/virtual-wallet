package com.company.web.wallet.models.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SavingsWalletDtoOut {
    @NotNull
    private String ownerUsername;
    @Positive
    private BigDecimal deposit;
    @Positive
    private int durationMonths;
    @NotNull
    private LocalDateTime endDate;
    @Positive
    private double interestRate;
    @Positive
    private BigDecimal interestReward;

    public SavingsWalletDtoOut() {
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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
}
