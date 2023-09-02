package com.company.web.wallet.models;

import javax.persistence.*;

@Entity
public class Pokes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poked_user_id")
    private User pokedUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poking_user_id")
    private User pokingUser;
    public Pokes() {
    }

    public Pokes(int id, User pokedUser, User pokingUser) {
        this.id = id;
        this.pokedUser = pokedUser;
        this.pokingUser = pokingUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPokedUser() {
        return pokedUser;
    }

    public void setPokedUser(User pokedUser) {
        this.pokedUser = pokedUser;
    }

    public User getPokingUser() {
        return pokingUser;
    }

    public void setPokingUser(User pokingUser) {
        this.pokingUser = pokingUser;
    }
}