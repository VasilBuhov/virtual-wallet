package com.company.web.wallet.services;

import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;

import java.util.List;

public interface CardService {
    Card get(int id);

    List<Card> getAll();

    void create(Card card);

    void update(int id, Card card, User user);

    void delete(int id, User user);
}