package com.company.web.wallet.models.DTO;

import java.math.BigDecimal;

public class CardTopUpDto {
    private String cardNumber;
    private BigDecimal amount;

    public CardTopUpDto() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setName(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
