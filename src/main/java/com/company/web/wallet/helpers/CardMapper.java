package com.company.web.wallet.helpers;

import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.CardDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class CardMapper {
    private final CardService service;

    @Autowired
    public CardMapper(CardService service) {
        this.service = service;
    }

    public Card createCardDto(CardDto cardDto, User user) {
        Card newCard = new Card();
        newCard.setCardHolder(user);
        newCard.setCardNumber(cardDto.getCardNumber());
        newCard.setStatusDeleted(0);
        newCard.setCheckNumber(cardDto.getCheckNumber());
        newCard.setExpirationDate(cardDto.getExpirationDate());
        newCard.setTransactionsSet(new HashSet<>());
        return newCard;
    }

    public Card updateCardDto(int id, CardDto cardDto, User user) {
        Card existingCard = service.get(id, user);
        existingCard.setExpirationDate(cardDto.getExpirationDate());
        existingCard.setCardNumber(cardDto.getCardNumber());
        existingCard.setCheckNumber(cardDto.getCheckNumber());
        return existingCard;
    }
}
