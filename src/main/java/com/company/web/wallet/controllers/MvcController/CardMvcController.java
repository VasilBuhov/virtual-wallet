package com.company.web.wallet.controllers.MvcController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cards")
public class CardMvcController {
    @GetMapping
public String getCards(HttpSession session){

    return "cards";
}
}
