package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.error.CustomNotFoundException;
import dev.chiptune.springboot.error.RestCustomNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class RestForceExceptionController {

    // 테스트 용 강제 에러 발생
    @PostMapping("/forceException")
    public String customNotFoundException() throws RestCustomNotFoundException {
        throw new RestCustomNotFoundException("Rest Force Exception!");
    }
}
