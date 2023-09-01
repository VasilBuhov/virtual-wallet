package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.models.User;
import com.company.web.wallet.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final UserService userService;

    public SearchController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String searchByUsername(@RequestParam("username") String username,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   Model model) {
        if (username.length() < 2) {
            return "errors/781";
        }
        Pageable pageable = PageRequest.of(page, size); // Set the pagination size here
        Page<User> usersPage = userService.findByUsernameContaining(username, pageable);
        model.addAttribute("usersByName", usersPage);
        int totalPages = usersPage.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        return "user_search_results";
    }

}
