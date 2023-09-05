package com.company.web.wallet.models;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "contact_owner")
    private int contactOwner;
    @Column(name = "contact_target")
    private int contactTarget;

    public Contact() {
    }

    public Contact(int id, int contactOwner, int contactTarget) {
        this.id = id;
        this.contactOwner = contactOwner;
        this.contactTarget = contactTarget;
    }

    public int getContactOwner() {
        return contactOwner;
    }

    public void setContactOwner(int contactOwner) {
        this.contactOwner = contactOwner;
    }

    public int getContactTarget() {
        return contactTarget;
    }

    public void setContactTarget(int contactTarget) {
        this.contactTarget = contactTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact that = (Contact) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

