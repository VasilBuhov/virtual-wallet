package com.company.web.wallet.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "authentication_pool")
public class AuthenticationPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "code")
    private int code;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    public AuthenticationPool() {
    }

    public AuthenticationPool(int userId, int code, LocalDateTime expiration) {
        this.userId = userId;
        this.code = code;
        this.expiration = expiration;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}