package com.company.web.wallet.models.DTO;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class WithdrawMoneyDto {
    @Positive
    private BigDecimal amount;

    public WithdrawMoneyDto() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
