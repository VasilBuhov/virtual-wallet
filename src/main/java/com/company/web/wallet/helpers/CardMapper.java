package com.company.web.wallet.helpers;

import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.DTO.CardDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    private final CardService cardService;

    @Autowired
    public CardMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public Card createCardDto(CardDto cardDto, User user) {
        Card newCard = new Card();
        newCard.setCardHolder(user);
        newCard.setCardNumber(cardDto.getCardNumber());
        newCard.setStatusDeleted(0);
        newCard.setCheckNumber(cardDto.getCheckNumber());
        newCard.setExpirationDate(cardDto.getExpirationDate());
        newCard.setName(cardDto.getName());
        return newCard;
    }

    public Card updateCardDto(int id, CardDto cardDto, User user) {
        Card existingCard = cardService.get(id, user);
        existingCard.setExpirationDate(cardDto.getExpirationDate());
        existingCard.setCardNumber(cardDto.getCardNumber());
        existingCard.setCheckNumber(cardDto.getCheckNumber());
        existingCard.setName(cardDto.getName());
        return existingCard;
    }
}
