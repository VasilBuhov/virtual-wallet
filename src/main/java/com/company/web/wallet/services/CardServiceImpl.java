package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private static final String MODIFY_CARD_ERROR_MESSAGE = "Only the card owner can modify the card information.";
    private final CardRepository repository;

    @Autowired
    public CardServiceImpl(CardRepository repository) {
        this.repository = repository;
    }

    @Override
    public Card get(int id) {
        Card card = repository.get(id);
        if (card.getStatusDeleted() == 1) {
            throw new EntityDeletedException("Card", "ID", String.valueOf(id));
        }
        return card;
    }

    @Override
    public List<Card> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(Card card) {
        Card existingCard = repository.get(card.getId());
        if (existingCard != null) {
            throw new EntityDuplicateException("Card", "card number", String.valueOf(existingCard.getCardNumber()));
        }
        repository.create(card);
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
