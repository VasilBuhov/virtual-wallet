package com.company.web.wallet.repositories;

import com.company.web.wallet.models.Card;

import java.util.List;

public interface CardRepository {
    Card get(int id);

    List<Card> getAll();

    void create(Card card);

    void update(Card card);

    void delete(int id);
}
