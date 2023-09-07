package com.company.web.wallet.models.DTO;


import javax.validation.constraints.NotNull;

public class UserLogin2FADto {

    @NotNull
    private String tfaCode;

   public String getTfaCode() {
        return tfaCode;
    }

    public void setTfaCode(String tfaCode) {
        this.tfaCode = tfaCode;
    }
}