package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;

import javax.persistence.NoResultException;
import java.util.List;

public interface CardService {
    Card get(int id, User user) throws EntityDeletedException, AuthorizationException, EntityNotFoundException;

    List<Card> getAll(User user) throws EntityNotFoundException;

    void create(Card card) throws EntityDuplicateException, EntityNotFoundException;

    void update(int id, Card card, User user)  throws AuthorizationException, EntityDuplicateException, EntityNotFoundException;

    void delete(int id, User user) throws AuthorizationException;
}
