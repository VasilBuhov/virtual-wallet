package com.company.web.wallet.models.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class SavingsWalletDtoIn {
    @Positive
    private BigDecimal deposit;
    @Positive
    private int durationMonths;
    @NotNull
    private int fromWalletNumber;

    public SavingsWalletDtoIn() {
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

    public int getFromWallet() {
        return fromWalletNumber;
    }

    public void setFromWallet(int fromWalletNumber) {
        this.fromWalletNumber = fromWalletNumber;
    }
}
