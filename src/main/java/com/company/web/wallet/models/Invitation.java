package com.company.web.wallet.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "invitations")
public class Invitation {
    private static final int EXPIRATION = 60 * 24 * 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "expire_date")
    private Date expiryDate;

    @Column(name = "used")
    private boolean isUsed;

    public Invitation() {
        this.isUsed = false;
        this.expiryDate = getExpirationDate(EXPIRATION);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    private Date getExpirationDate(int timeInMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, timeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
}
