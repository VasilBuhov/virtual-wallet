package com.company.web.wallet.models.DTO;

import java.math.BigDecimal;

public class CardTopUpDto {
    private String name;
    private BigDecimal amount;

    public CardTopUpDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
