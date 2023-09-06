package com.company.web.wallet.models.DTO;

import javax.validation.constraints.Email;

public class InvitationDto {

    @Email
    String email;

    public InvitationDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
