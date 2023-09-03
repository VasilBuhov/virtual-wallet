package com.company.web.wallet.models.DTO;

import com.company.web.wallet.validators.FutureDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class CardDto {
    @NotNull(message = "Card number can not be empty.")
    @Pattern(regexp = "(^[0-9]{16}$)", message = "Card number should be exactly 16 digits.")
    private String cardNumber;
    @Size(min = 2, max = 30, message = "Card holder should be between 2 and 30 symbols.")
    private String name;
    @NotNull(message = "Please enter date in this format MM/YY ")
    @Pattern(regexp = "(?:0[1-9]|1[0-2])/[0-9]{2}", message = "Expiration date should be in this format MM/YY ")
    private String expirationDate;
    @NotNull(message = "CCV can not be empty.")
    @Pattern(regexp = "(^[0-9]{3}$)", message = "CVV should be  exactly 3 digits.")
    private int checkNumber;

    public CardDto() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }
}
