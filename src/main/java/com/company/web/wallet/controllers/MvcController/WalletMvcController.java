package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.WalletMapper;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.DTO.CardDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/new")
    public String showCreateWalletForm(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("walletDto", new CardDto());
        return "create_walllet";
    }

    @PostMapping("/new")
    public String createWallet(
                             Model model,
                             HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Wallet wallet = walletMapper.createWalletDto(user);
            walletService.create(wallet);
            return "redirect:/wallets";
        } catch (AuthenticationFailureException | AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }
}
