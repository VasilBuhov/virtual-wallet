package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import javax.persistence.NoResultException;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTests {

    private CardRepository cardRepositoryMock;
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        cardRepositoryMock = Mockito.mock(CardRepository.class);
        cardService = new CardServiceImpl(cardRepositoryMock);
    }

    @Test
    void CardServiceImpl_Get_ReturnsCardWhenSuccessful() {
        User mockUser = new User();
        Card mockCard = new Card();
        mockCard.setCardHolder(mockUser);

        when(cardRepositoryMock.get(anyInt())).thenReturn(mockCard);

        Card result = cardService.get(1, mockUser);

        assertNotNull(result);
        assertEquals(mockCard, result);
    }

    @Test
    void CardServiceImpl_Get_ThrowsAuthorizationExceptionWhenInvalidUser() {
        User mockUser = mock(User.class);
        User anotherUser = mock(User.class);
        Card mockCard = mock(Card.class);

        when(mockCard.getCardHolder()).thenReturn(anotherUser);
        when(cardRepositoryMock.get(anyInt())).thenReturn(mockCard);

        assertThrows(AuthorizationException.class, () -> {
            cardService.get(1, mockUser);
        });
    }

    @Test
    void CardServiceImpl_GetAll_ReturnsAllCardsForUser() {
        User mockUser = new User();
        Card mockCard = new Card();
        mockCard.setCardHolder(mockUser);

        when(cardRepositoryMock.getAll()).thenReturn(Collections.singletonList(mockCard));

        var result = cardService.getAll(mockUser);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockCard, result.get(0));
    }

    @Test
    void CardServiceImpl_Create_CreatesCardSuccessfully() {
        Card mockCard = new Card();
        mockCard.setCardNumber("123456");

        when(cardRepositoryMock.get("123456")).thenThrow(NoResultException.class);

        assertDoesNotThrow(() -> {
            cardService.create(mockCard);
        });
    }

    @Test
    void CardServiceImpl_Create_ThrowsEntityDuplicateExceptionWhenDuplicateExists() {
        Card mockCard = new Card();
        mockCard.setCardNumber("123456");

        when(cardRepositoryMock.get("123456")).thenReturn(mockCard);

        assertThrows(EntityDuplicateException.class, () -> {
            cardService.create(mockCard);
        });
    }

    @Test
    void CardServiceImpl_Delete_ThrowsAuthorizationExceptionWhenInvalidUser() {

        User mockUser = mock(User.class);
        User anotherUser = mock(User.class);
        Card mockCard = mock(Card.class);

        when(mockCard.getCardHolder()).thenReturn(anotherUser);
        when(cardRepositoryMock.get(anyInt())).thenReturn(mockCard);

        assertThrows(AuthorizationException.class, () -> {
            cardService.delete(1, mockUser);
        });

    }

    @Test
    public void CardServiceImpl_Update_UpdatesSuccessfullyWhenCorrectUser() {
        int cardId = 1;
        User mockUser = mock(User.class);
        Card mockCard = mock(Card.class);

        when(mockCard.getCardHolder()).thenReturn(mockUser);
        when(cardRepositoryMock.get(cardId)).thenReturn(mockCard);

        cardService.update(cardId, mockCard, mockUser);

        verify(cardRepositoryMock).get(cardId);
        verify(cardRepositoryMock).update(mockCard);
    }


    @Test
    public void CardServiceImpl_Update_ThrowsAuthorizationExceptionWhenInvalidUser() {
        int cardId = 1;
        User mockUser = mock(User.class);
        User anotherUser = mock(User.class);
        Card mockCard = mock(Card.class);

        when(mockCard.getCardHolder()).thenReturn(anotherUser);
        when(cardRepositoryMock.get(cardId)).thenReturn(mockCard);

        assertThrows(AuthorizationException.class, () -> cardService.update(cardId, mockCard, mockUser));
        verify(cardRepositoryMock).get(cardId);
        verifyNoMoreInteractions(cardRepositoryMock);
    }
}

