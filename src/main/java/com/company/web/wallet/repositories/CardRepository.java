package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;

import java.util.List;

public interface CardRepository {
    Card get(int id) throws EntityNotFoundException;
    Card get(String cardNumber);

    List<Card> getAll();

    void create(Card card);

    void update(Card card);

    void delete(int id);
}
