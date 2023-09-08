package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.UnauthorizedOperationException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.TopUpHelper;
import com.company.web.wallet.helpers.WalletMapper;
import com.company.web.wallet.models.DTO.CardTopUpDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.services.CardService;
import com.company.web.wallet.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/wallets")
public class WalletMvcController {
    private final WalletService walletService;
    private final CardService cardService;
    private final AuthenticationHelper authenticationHelper;
    private final TopUpHelper topUpHelper;
    private final WalletMapper walletMapper;
    private final Logger logger = LoggerFactory.getLogger(CardMvcController.class);

    @Autowired
    public WalletMvcController(WalletService walletService, CardService cardService, AuthenticationHelper authenticationHelper, TopUpHelper topUpHelper, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.cardService = cardService;
        this.authenticationHelper = authenticationHelper;
        this.topUpHelper = topUpHelper;
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
    @GetMapping("/{id}/overdraft")
    public String showEditWalletForm(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            return enableOverdraft(id, model, session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/overdraft")
    public String enableOverdraft(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            walletService.updateOverdraft(id, user);
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
    @GetMapping("/top-up/{id}")
    public String showTopUpForm(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Wallet walletToBeToppedUp = walletService.get(id, user);
            model.addAttribute("CardTopUpDto", new CardTopUpDto());
            model.addAttribute("wallet", walletToBeToppedUp);
            return "wallet_top_up";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/top-up/")
    public String topUp(@Valid @ModelAttribute("CardTopUpDto") CardTopUpDto cardTopUpDto,
                        @RequestParam("walletId") int walletId,
                        Model model,
                        HttpSession session) {

        try {
            User user = authenticationHelper.tryGetUser(session);
//            Card cardForTopUp = cardService.get(cardTopUpDto.getCardNumber(), user);
            try {
                topUpHelper.tryTopUp();
            } catch (HttpClientErrorException e) {
                return "redirect:/wallet_top_up";
            }
            walletService.addToBalance(walletId, user, cardTopUpDto.getAmount());
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
    @GetMapping("/delete/{id}")
    public String deleteWallet(@PathVariable int id, Model model, HttpSession session) {
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
