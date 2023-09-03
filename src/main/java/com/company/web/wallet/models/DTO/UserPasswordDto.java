package com.company.web.wallet.models.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserPasswordDto {
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$",
            message = "Password must be min 8 and max 20 length containing at least " +
                    "1 uppercase letter, 1 special character and 1 digit ")
    private String currentPassword;
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$",
            message = "Password must be min 8 and max 20 length containing at least " +
                    "1 uppercase letter, 1 special character and 1 digit ")
    private String newPassword;
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$",
            message = "Password must be min 8 and max 20 length containing at least " +
                    "1 uppercase letter, 1 special character and 1 digit ")
    private String passwordConfirm;

    public UserPasswordDto() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}



