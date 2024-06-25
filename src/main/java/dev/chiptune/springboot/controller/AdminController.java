package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.entity.User;
import dev.chiptune.springboot.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String getAdminUser(
            @RequestParam(required = false) String userId,
            Model model
    ) {
        model.addAttribute("list", userRepository.findByUserIdAndUserType(userId, "admin"));
        return "admin";
    }
}
