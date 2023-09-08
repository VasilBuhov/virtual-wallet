package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.controllers.RestController.WalletRestController;
import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.InterestRateMapper;
import com.company.web.wallet.models.DTO.CardDto;
import com.company.web.wallet.models.DTO.InterestRateDto;
import com.company.web.wallet.models.InterestRate;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.InterestRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/interest")
public class InterestRateMvcController {
    private final InterestRateMapper interestRateMapper;
    private final InterestRateService interestRateService;
    private final AuthenticationHelper authenticationHelper;
    private final Logger logger = LoggerFactory.getLogger(WalletRestController.class);

    @Autowired
    public InterestRateMvcController(InterestRateMapper interestRateMapper, InterestRateService interestRateService, AuthenticationHelper authenticationHelper) {
        this.interestRateMapper = interestRateMapper;
        this.interestRateService = interestRateService;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping("/change")
    public String updateInterestRateForm(HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetUser(session);
            double currentInterestRate = interestRateService.getLatestInterestRate();
            model.addAttribute("currentInterestRate", currentInterestRate);
            model.addAttribute("interestRateDto", new InterestRateDto());
            return "admin_change_interest";
        } catch (AuthorizationException | BlockedUserException | AuthenticationFailureException e) {
            logger.error(e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/change")
    public String updateInterestRate(@Valid @ModelAttribute("interestRateDto") InterestRateDto interestRateDto, HttpSession session, Model model) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(session);
            InterestRate interestRate = interestRateMapper.create(interestRateDto);
            interestRateService.create(interestRate, user);
            return "admin_change_interest";
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "unauthorized_view";
        }
    }
}
