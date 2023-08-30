package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.models.DTO.UserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model)
    {
        model.addAttribute("login", new UserLoginDto());
        return "user_login";
    }

}
