package com.company.web.wallet.models.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UserRecipientDto {

    @NotEmpty(message = "Recipient username, phone number, or email is required")
    private String recipientIdentifier; // Can be phone number, username, or email




    public String getRecipientIdentifier() {
        return recipientIdentifier;
    }

    public void setRecipientIdentifier(String recipientIdentifier) {
        this.recipientIdentifier = recipientIdentifier;
    }


}
