package com.company.web.wallet.models;

import javax.persistence.*;

@Entity
@Table(name = "photo_verification")
public class PhotoVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private int userId;

    @Lob
    @Column(name = "id_card")
    private byte[] idCard;

    @Lob
    @Column(name = "selfie")
    private byte[] selfie;

    public PhotoVerification() {
    }

    public PhotoVerification(int id, int userId, byte[] idCard, byte[] selfie) {
        this.id = id;
        this.userId = userId;
        this.idCard = idCard;
        this.selfie = selfie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

   public byte[] getIdCard() {
        return idCard;
    }

    public void setIdCard(byte[] idCard) {
        this.idCard = idCard;
    }

    public byte[] getSelfie() {
        return selfie;
    }

    public void setSelfie(byte[] selfie) {
        this.selfie = selfie;
    }
}
