package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.*;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.CardMapper;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.CardDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.CardService;
import com.company.web.wallet.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

//TODO Need to implement catching for Authorization exceptions after Nikolai fixes the Authentication helper.
@RestController
@RequestMapping("/api/cards")
public class CardRestController {
    private final CardService cardService;
    private final AuthenticationHelper authenticationHelper;
    private final CardMapper cardMapper;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(CardRestController.class);


    @Autowired
    public CardRestController(CardService cardService, AuthenticationHelper authenticationHelper, CardMapper cardMapper, UserService userService) {
        this.cardService = cardService;
        this.authenticationHelper = authenticationHelper;
        this.cardMapper = cardMapper;
        this.userService = userService;
    }

    @GetMapping
    public List<Card> get(@RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return cardService.getAll(user);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Card get(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return cardService.get(id, user);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Card create(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody CardDto cardDto) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Card card = cardMapper.createCardDto(cardDto, user);
            cardService.create(card);
            userService.addCard(card, user);
            return card;
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException | EntityDuplicateException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Card update(@PathVariable int id, @RequestHeader HttpHeaders httpheaders, @Valid @RequestBody CardDto cardDto) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            Card card = cardMapper.updateCardDto(id, cardDto, user);
            cardService.update(id, card, user);
            return card;
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            cardService.delete(id, user);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
