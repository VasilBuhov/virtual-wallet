package com.company.web.wallet.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class InterestRateDto {
    @NotNull
    @Positive
    private double interest;

    public InterestRateDto() {
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
