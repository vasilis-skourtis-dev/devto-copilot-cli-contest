package org.vs.valentine.cards.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page and main navigation.
 */
@Controller
public class HomeController {

    /**
     * Displays the home page with the closed Valentine card.
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Valentine Cards - Send Secret Messages");
        return "home";
    }

    /**
     * Displays the main card interface (opened card).
     */
    @GetMapping("/card")
    public String card(Model model) {
        model.addAttribute("pageTitle", "Valentine Cards - Read or Create");
        return "card";
    }
}
