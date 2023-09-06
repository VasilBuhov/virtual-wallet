package com.company.web.wallet.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "verified")
    private int verified;
    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_level")
    private int userLevel;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;
    @JsonIgnore
    @OneToMany(mappedBy = "cardHolder", fetch = FetchType.EAGER)
    private Set<Card> cards;
    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Wallet> wallets;
    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<SavingsWallet> savingsWallets;

    @OneToMany(mappedBy = "pokedUser", fetch = FetchType.EAGER)
    private Set<Pokes> pokes;
    private boolean enabled;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdateDate;
    @Column(name = "status_deleted")
    private boolean statusDeleted;

    @Column(name = "photo_verification")
    private boolean photoVerification;

    public User() {
    }

    public User(int id, String firstName, String lastName, String username, String email, String password, String phone,
                int userLevel, int verified, byte[] avatar, Set<Pokes> pokes, boolean statusDeleted, boolean photoVerification) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.profilePicture = avatar;
        this.phone = phone;
        this.pokes = pokes;
        this.statusDeleted = statusDeleted;
        this.photoVerification = photoVerification;

        setUserLevel(userLevel);
    }

    public int getUserLevel() {
        return userLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] avatar) {
        this.profilePicture = avatar;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Set<Pokes> getPokes() {
        return pokes;
    }

    public void setPokes(Set<Pokes> pokes) {
        this.pokes = pokes;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isStatusDeleted() {
        return statusDeleted;
    }

    public void setStatusDeleted(boolean statusDeleted) {
        this.statusDeleted = statusDeleted;
    }

    public boolean isPhotoVerified() {
        return photoVerification;
    }

    public void setPhotoVerification(boolean photoVerification) {
        this.photoVerification = photoVerification;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
