package com.company.web.wallet.services;

import com.company.web.wallet.controllers.MvcController.CardMvcController;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.CardExpiration;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.CardRepository;
import com.company.web.wallet.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private static final String MODIFY_CARD_ERROR_MESSAGE = "Only the card owner can modify the card information.";
    public static final String CARD_HAS_EXPIRED = "Card has passed the expiration date determined by the credit card provider";
    private final CardRepository repository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    public CardServiceImpl(CardRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
    public Card get(String cardNumber, User user) throws EntityNotFoundException {
        Card card = repository.get(cardNumber);
        if (card != null && card.getCardHolder().equals(user)) {
            return card;
        } else {
            throw new EntityNotFoundException("Card", "card number", String.valueOf(cardNumber));
        }
    }

    @Override
    public void create(Card card) {
        if (CardExpiration.isExpired(card.getExpirationDate())) {
            throw new EntityNotFoundException(CARD_HAS_EXPIRED);
        }
        Card existingCard = repository.get(card.getCardNumber());
        if (existingCard != null) {
            throw new EntityDuplicateException("Card", "card number", String.valueOf(existingCard.getCardNumber()));
        } else {
            repository.create(card);
        }
    }

    @Override
    public void update(int id, Card card, User user) {
        checkModifyPermissions(id, user);
        if (CardExpiration.isExpired(card.getExpirationDate())) {
            throw new EntityNotFoundException(CARD_HAS_EXPIRED);
        }
        boolean isDuplicateCardNumber = true;
        try {
            Card duplicateCard = repository.get(card.getCardNumber());
            if (duplicateCard.getId() == card.getId()) {
                isDuplicateCardNumber = false;
            }
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            isDuplicateCardNumber = false;
        }

        if (isDuplicateCardNumber) {
            throw new EntityDuplicateException("Card", "card number", card.getCardNumber());
        }
        repository.update(card);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        user.getCards().remove(repository.get(id));
        userRepository.update(user);
        repository.delete(id);
    }

    private void checkModifyPermissions(int cardId, User user) {
        Card card = repository.get(cardId);
        if (!card.getCardHolder().equals(user)) {
            throw new AuthorizationException(MODIFY_CARD_ERROR_MESSAGE);
        }
    }
}
