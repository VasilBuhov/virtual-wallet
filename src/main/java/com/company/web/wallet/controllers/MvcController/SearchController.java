package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final UserService userService;

    public SearchController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String searchByTopicName (@RequestParam("search") String username, Model model) {
        if (username.length() < 2) return "errors/781";
        model.addAttribute("usersByName", userService.get(username));
        return "user_search_results";
    }
}
