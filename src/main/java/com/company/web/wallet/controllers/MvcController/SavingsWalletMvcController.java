package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.UnauthorizedOperationException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.SavingsWalletMapper;
import com.company.web.wallet.models.DTO.SavingsWalletDtoIn;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.SavingsWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/savings")
public class SavingsWalletMvcController {
    private final SavingsWalletService savingsWalletService;
    private final SavingsWalletMapper savingsWalletMapper;
    private final AuthenticationHelper authenticationHelper;
    private final Logger logger = LoggerFactory.getLogger(CardMvcController.class);

    @Autowired
    public SavingsWalletMvcController(SavingsWalletService savingsWalletService, SavingsWalletMapper savingsWalletMapper, AuthenticationHelper authenticationHelper) {
        this.savingsWalletService = savingsWalletService;
        this.savingsWalletMapper = savingsWalletMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping
    public String showWallets(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            List<SavingsWallet> savingsWallets = savingsWalletService.getAllForUser(user);
            model.addAttribute("savingsWallets", savingsWallets);
            return "savings_wallets_list";
        } catch (AuthorizationException | BlockedUserException | AuthenticationFailureException e) {
            logger.error(e.getMessage());
            return "redirect:/auth/login";
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            return "user_no_savings_wallets";
        }
    }
    @GetMapping("/new")
    public String showCreateSavingsWalletForm(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("savingsWalletDtoIn", new SavingsWalletDtoIn());
            return "create_savings_wallet";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createSavingsWallet(Model model, HttpSession session, @Valid @ModelAttribute SavingsWalletDtoIn savingsWalletDtoIn, BindingResult bindingResult) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (bindingResult.hasErrors()) {
                return "create_savings_wallet";
            }
            SavingsWallet savingsWallet = savingsWalletMapper.createSavingsWalletFromDto(savingsWalletDtoIn, user);
            savingsWalletService.create(user, savingsWallet);
            return "redirect:/savings";
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
    public String deleteSavingsWallet(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            savingsWalletService.delete(id, user);
            return "redirect:/savings_wallets_list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
    }
}
