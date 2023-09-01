package com.company.web.wallet.models.DTO;

import javax.validation.constraints.NotNull;

public class UserLoginDto {
    @NotNull
    private String username;
    @NotNull
    private String password;

    private int userLevel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
}