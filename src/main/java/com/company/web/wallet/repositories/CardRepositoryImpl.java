package com.company.web.wallet.repositories;

import com.company.web.wallet.controllers.RestController.CardRestController;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CardRepositoryImpl implements CardRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Card get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Card card = session.get(Card.class, id);
            if (card == null) {
                throw new EntityNotFoundException("Card", id);
            }
            return card;
        }
    }

    @Override
    public Card get(String cardNumber) {
        Session session = sessionFactory.openSession();
        return session.createQuery("FROM Card WHERE cardNumber = :cardNumber AND statusDeleted = 0", Card.class)
                .setParameter("cardNumber", cardNumber)
                .uniqueResult();
    }

    @Override
    public List<Card> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Card", Card.class)
                    .list()
                    .stream()
                    .filter(card -> card.getStatusDeleted() == 0)
                    .collect(Collectors.toList());
        }
    }


    @Override
    public void create(Card card) {
        try (Session session = sessionFactory.openSession()) {
            session.save(card);
        }
    }

    @Override
    public void update(Card card) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(card);
            session.getTransaction().commit();
        }
    }

    public void delete(int id) {
        Card cardToDelete = get(id);
        cardToDelete.setStatusDeleted(1);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(cardToDelete);
            session.getTransaction().commit();
        }
    }
}
