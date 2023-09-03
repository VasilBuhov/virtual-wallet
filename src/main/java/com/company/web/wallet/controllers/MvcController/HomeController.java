package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.models.ContactForm;
import com.company.web.wallet.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showHomePage(Model model) {

        model.addAttribute("contactForm", new ContactForm());
        List<String> imageFilenames = Arrays.asList(
                "team_ceo.jpg", "team_coo.jpg",
                "team_finance_manager.jpg", "team_hr.jpg",
                "team_accountant.jpg", "team_audit.jpg",
                "team_sales.jpg", "team_secretory.jpg",
                "team_developer.jpg", "team_it.jpg",
                "team_marketing.jpg", "team_supply_chain.jpg",
                //end sq

                "team_strategy_head.jpg",
                "team_media_planner.jpg", "team_intern.jpg",
                "team_copywriter.jpg", "team_creative_director.jpg",
                "team_art_director.jpg", "team_account_executive.jpg");
        model.addAttribute("imageFilenames", imageFilenames);

        return "index";
    }

    @GetMapping("/team")
    public String showTeamPage(Model model) {
        List<String> imageFilenames = Arrays.asList(
                "team_ceo.jpg", "team_coo.jpg",
                "team_finance_manager.jpg", "team_hr.jpg",
                "team_accountant.jpg", "team_audit.jpg",
                "team_sales.jpg", "team_secretory.jpg",
                "team_developer.jpg", "team_it.jpg",
                "team_marketing.jpg", "team_supply_chain.jpg",
                "team_strategy_head.jpg",
                "team_media_planner.jpg", "team_intern.jpg",
                "team_copywriter.jpg", "team_creative_director.jpg",
                "team_art_director.jpg", "team_account_executive.jpg");
        model.addAttribute("imageFilenames", imageFilenames);
        return "site/team";
    }

    @GetMapping("/terms")
    public String showTermsPage() {
        return "site/terms";
    }

    @GetMapping("/about")
    public String showAboutUsPage() {
        return "site/about_us";
    }

    @GetMapping("/faq")
    public String showFAQPage() {
        return "site/faq";
    }

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "site/contact";
    }

    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute("contactForm") ContactForm contactForm) throws MessagingException, UnsupportedEncodingException {
        userService.sendContactEmail(contactForm);
        return "user_contact_success";
    }
}
