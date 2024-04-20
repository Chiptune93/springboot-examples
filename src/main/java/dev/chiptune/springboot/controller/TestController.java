package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.repo.TestUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestUserRepository testUserRepository;

    public TestController(TestUserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    @GetMapping("")
    public String test() {
        return testUserRepository.findAll().toString();
    }
}
