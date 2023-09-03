package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.*;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.CardMapper;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.DTO.CardDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardMvcController {
    private final CardService cardService;
    private final AuthenticationHelper authenticationHelper;
    private final CardMapper cardMapper;
    private final Logger logger = LoggerFactory.getLogger(CardMvcController.class);

    @Autowired
    public CardMvcController(CardService cardService, AuthenticationHelper authenticationHelper, CardMapper cardMapper) {
        this.cardService = cardService;
        this.authenticationHelper = authenticationHelper;
        this.cardMapper = cardMapper;
    }

    @GetMapping
    public String showCards(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            List<Card> cards = cardService.getAll(user);
            model.addAttribute("cards", cards);
            return "cards_list";
        } catch (AuthorizationException | BlockedUserException | AuthenticationFailureException e) {
            logger.error(e.getMessage());
            return "redirect:/auth/login";
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String showCard(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Card card = cardService.get(id, user);
            model.addAttribute("card", card);
            return "card_details"; // Return the name of the view template for card details
        } catch (AuthorizationException | BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/new")
    public String showCreateCardForm(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("cardDto", new CardDto());
        return "create_card";
    }

    @PostMapping("/new")
    public String createCard(@Valid @ModelAttribute("cardDto") CardDto cardDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);

            if (bindingResult.hasErrors()) {
                return "create_card";
            }

            Card card = cardMapper.createCardDto(cardDto, user);
            cardService.create(card);

            return "redirect:/cards";
        } catch (AuthenticationFailureException | AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (EntityDuplicateException e) {
            logger.error(e.getMessage());
            bindingResult.rejectValue("cardNumber", "cardNumber", e.getMessage());
            return "create_card";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditCardForm(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Card card = cardService.get(id, user);
            model.addAttribute("card", card);
            return "edit_card_form"; // Return the name of the view template for editing a card
        } catch (AuthorizationException | BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{id}/edit")
    public String editCard(@PathVariable int id, @Valid @ModelAttribute("cardDto") CardDto cardDto,
                           BindingResult bindingResult,
                           Model model,
                           HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);

            if (bindingResult.hasErrors()) {
                return "edit_card_form";
            }

            Card card = cardMapper.updateCardDto(id, cardDto, user);
            cardService.update(id, card, user);
            return "redirect:/cards";
        } catch (AuthorizationException | BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
