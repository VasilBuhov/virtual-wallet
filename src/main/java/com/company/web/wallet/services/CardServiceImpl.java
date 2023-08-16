package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private static final String MODIFY_CARD_ERROR_MESSAGE = "Only the card owner can modify the card information.";
    private final CardRepository repository;

    @Autowired
    public CardServiceImpl(CardRepository repository) {
        this.repository = repository;
    }

    @Override
    public Card get(int id, User user) {
        checkModifyPermissions(id, user);
        Card card = repository.get(id);
        if (card.getStatusDeleted() == 1) {
            throw new EntityDeletedException("Card", "ID", String.valueOf(id));
        }
        return card;
    }

    @Override
    public List<Card> getAll(User user) {
        List<Card> result = repository.getAll();
        List<Card> matchingCards = result.stream()
                .filter(card -> card.getCardHolder().equals(user))
                .collect(Collectors.toList());
        if (matchingCards.isEmpty()) {
            throw new EntityNotFoundException("Cards", "card holder", user.getUsername());
        }
        return matchingCards;
    }

    @Override
    public void create(Card card) {
        try {
            Card existingCard = repository.get(card.getCardNumber());
            if (existingCard != null) {
                throw new EntityDuplicateException("Card", "card number", String.valueOf(existingCard.getCardNumber()));
            }
        } catch (NoResultException e) {
            repository.create(card);
        }
    }

    @Override
    public void update(int id, Card card, User user) {
        checkModifyPermissions(id, user);
        repository.update(card);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        repository.delete(id);
    }

    private void checkModifyPermissions(int cardId, User user) {
        Card card = repository.get(cardId);
        if (!card.getCardHolder().equals(user)) {
            throw new AuthorizationException(MODIFY_CARD_ERROR_MESSAGE);
        }
    }
}
