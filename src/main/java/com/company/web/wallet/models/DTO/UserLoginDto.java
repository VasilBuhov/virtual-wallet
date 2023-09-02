package com.company.web.wallet.models.DTO;

import com.company.web.wallet.models.Pokes;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class UserLoginDto {
    @NotNull
    private String username;
    @NotNull
    private String password;

    private int userLevel;

    private Set<Pokes> pokes;

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

    public Set<Pokes> getPokes() {
        return pokes;
    }

    public void setPokes(Set<Pokes> pokes) {
        this.pokes = pokes;
    }
}