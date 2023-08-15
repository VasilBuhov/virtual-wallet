package com.company.web.wallet.models;

import com.company.web.wallet.validators.FutureDate;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDto {
    @Size(min = 16, max = 16, message = "Card number must be 16 digits.")
    private double cardNumber;
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 symbols long.")
    private String ownerName;
    @FutureDate(message = "Expiration date must be in the future")
    private LocalDate expirationDate;
    @Size(min = 3, max = 3, message = "Check number must be 16 digits.")
    private int checkNumber;

    public CardDto() {
    }

    public double getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(double cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }
}
