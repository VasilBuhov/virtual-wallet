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
        System.out.println(session.getId());
        System.out.println(session.getAttribute("name"));
        if (session.getId().equals("FDCBCDAFDC43B4E0FE9B47A10EA05A49")) {
            session.setAttribute("name", "Svetoslav");

        }
    return "cards";
}
}
