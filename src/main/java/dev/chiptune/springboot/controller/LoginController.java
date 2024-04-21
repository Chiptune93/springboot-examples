package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.entity.TestUser;
import dev.chiptune.springboot.repo.TestUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {


    private final TestUserRepository testUserRepository;

    public LoginController(TestUserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    @GetMapping
    @RequestMapping("/page")
    public String login(Model model, HttpServletRequest request) {
        return "login";
    }

    @PostMapping("/loginProcess")
    public String join(@ModelAttribute("username") String username,
                       @ModelAttribute("password") String password,
                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // 사용자 체크
            TestUser testUser = testUserRepository.findByNameAndPassword(username, password);
            if (testUser != null) {
                // 세션을 생성하기 전에 기존의 세션 파기
                request.getSession().invalidate();
                session = request.getSession(true);  // Session이 없으면 생성
                // 세션에 userId를 넣어줌
                session.setAttribute("loginUser", testUser);
                session.setMaxInactiveInterval(1800); // Session이 30분동안 유지
            }
        }
        return "redirect:/login/page";
    }
}
