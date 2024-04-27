package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.error.CustomNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ForceExceptionController {

    // 테스트용 강제 에러 발생
    @GetMapping("/forceException")
    String customNotFoundException(Model model) throws CustomNotFoundException {
        throw new CustomNotFoundException("Force Exception!");
    }

    // 시큐리티에서 해당 에러가 발생하는 경우 여기로 요청을 보내 강제로 어플리케이션 내부에서 에러 발생하도록 유도.
    @GetMapping("/BadCredentialsException")
    String badCredentialsException(Model model) throws BadCredentialsException {
        throw new BadCredentialsException("Force BadCredentialsException!");
    }
}
