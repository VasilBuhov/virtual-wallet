package com.company.web.wallet.models.DTO;


import javax.validation.constraints.NotNull;


public class WalletDtoIn {
    @NotNull
    private int overdraftEnabled;

    public WalletDtoIn() {
    }

    public int getOverdraftEnabled() {
        return overdraftEnabled;
    }

    public void setOverdraftEnabled(int overdraftEnabled) {
        this.overdraftEnabled = overdraftEnabled;
    }

}
