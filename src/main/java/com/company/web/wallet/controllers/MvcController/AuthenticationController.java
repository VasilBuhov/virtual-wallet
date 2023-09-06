package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.models.DTO.UserLoginDto;
import com.company.web.wallet.models.Pokes;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationHelper authenticationHelper;

    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model)
    {
        model.addAttribute("login", new UserLoginDto());
        return "user_login";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session)
    {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") UserLoginDto dto, BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "user_login";
        }

        try {
            authenticationHelper.verifyAuthentication(dto.getUsername(), dto.getPassword(), dto.getUserLevel());
            User user = userService.getByUsername(dto.getUsername());
            if (user.getVerified() != 1) return "redirect:/user_unverified";
            session.setAttribute("currentUser", dto.getUsername());
            session.setAttribute("currentUserLevel", user.getUserLevel());
            for (Pokes poke : user.getPokes()) {
                System.out.println("Poke from: " + poke.getPokingUser().getUsername());
            }
            session.setAttribute("pokes", user.getPokes());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth error", e.getMessage());
            return "user_login";
        } catch (BlockedUserException e) {
            bindingResult.rejectValue("username", "auth error", e.getMessage());
            return "user_blocked";
        }
    }

}
