package dev.chiptune.springboot.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class CommonController {

    @GetMapping("/home")
    String home(Model model) {
        System.out.println("------------------------------");
        System.out.println("home : " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("home : " + SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        System.out.println("------------------------------");
        // model.addAttribute("authentication", SecurityContextHolder.getContext().getAuthentication());
        return "home";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

}
