package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.UnauthorizedOperationException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.WalletMapper;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/wallets")
public class WalletMvcController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final WalletMapper walletMapper;
    private final Logger logger = LoggerFactory.getLogger(CardMvcController.class);

    @Autowired
    public WalletMvcController(WalletService walletService, AuthenticationHelper authenticationHelper, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.walletMapper = walletMapper;
    }
    @GetMapping
    public String showWallets(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            List<Wallet> wallets = walletService.getAll(user);
            model.addAttribute("wallets", wallets);
            return "wallets_list";
        } catch (AuthorizationException | BlockedUserException | AuthenticationFailureException e) {
            logger.error(e.getMessage());
            return "redirect:/auth/login";
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            return "user_no_wallets";
        }
    }
    @GetMapping("/new")
    public String showCreateWalletForm(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            return createWallet(model, session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createWallet(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Wallet wallet = walletMapper.createWalletDto(user);
            walletService.create(wallet);
            return "redirect:/wallets";
        } catch (AuthenticationFailureException | AuthorizationException e) {
            logger.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }
//    @GetMapping("/{id}/update")
//    public String showEditWalletForm(@PathVariable int id, Model model, HttpSession session) {
//        try {
//            authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            logger.error(e.getMessage());
//            return "redirect:/auth/login";
//        }
//        User user = authenticationHelper.tryGetUser(session);
//        Wallet wallet = walletService.get(id, user);
//        WalletDtoIn walletDtoUpdate = walletMapper.updateOverdraftDto(id, user);
//        model.addAttribute("cardDtoUpdate", cardDtoUpdate);
//        return "card_update";
//    }
//
//    @PostMapping("/{id}/update")
//    public String editCard(@PathVariable int id, @Valid @ModelAttribute("cardDtoUpdate") CardDto cardDto,
//                           BindingResult bindingResult,
//                           Model model,
//                           HttpSession session) {
//        try {
//            User user = authenticationHelper.tryGetUser(session);
//
//            if (bindingResult.hasErrors()) {
//                return "card_update";
//            }
//
//            Card card = cardMapper.updateCardDto(id, cardDto, user);
//            cardService.update(id, card, user);
//            return "redirect:/cards";
//        } catch (AuthorizationException | BlockedUserException e) {
//            logger.error(e.getMessage());
//            model.addAttribute("error", e.getMessage());
//            return "access_denied";
//        } catch (EntityDuplicateException e) {
//            logger.error(e.getMessage());
//            bindingResult.rejectValue("cardNumber", "cardNumber", e.getMessage());
//            return "card_update";
//        } catch (EntityNotFoundException e) {
//            logger.error(e.getMessage());
//            model.addAttribute("error", e.getMessage());
//            return "errors/404";
//        }
//    }
    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            walletService.delete(id, user);
            return "redirect:/wallets";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
    }
}
