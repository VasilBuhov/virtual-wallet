package com.company.web.wallet.models.DTO;

import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.Pokes;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.Wallet;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserDto {

    @NotNull(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")

    private int userLevel;
    private String phone;
    private String password;

    private byte[] avatar;

    private Set<Pokes> pokes;

    private boolean TFA;
    private Set<Card> cards;
    private Set<Wallet> wallets;
    private Set<SavingsWallet> savingsWallets;

    public UserDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Pokes> getPokes() {
        return pokes;
    }

    public void setPokes(Set<Pokes> pokes) {
        this.pokes = pokes;
    }

    public boolean getTFA() {
        return TFA;
    }

    public void setTFA(boolean TFA) {
        this.TFA = TFA;
    }

    public boolean isTFA() {
        return TFA;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }

    public Set<SavingsWallet> getSavingsWallets() {
        return savingsWallets;
    }

    public void setSavingsWallets(Set<SavingsWallet> savingsWallets) {
        this.savingsWallets = savingsWallets;
    }
}