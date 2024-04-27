package dev.chiptune.springboot.config.errorhandler;

import dev.chiptune.springboot.error.CustomNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    // 커스텀 예외를 핸들링하는 메소드
    @ExceptionHandler(CustomNotFoundException.class)
    public String handleCustomNotFoundException(Model model, CustomNotFoundException ex) {
        model.addAttribute("error", ex.getMessage());
        return "/error/CustomNotFound";
    }

    // 시큐리티 인증 과정의 에러를 처리하기 위한 핸들링 옵션
    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException e) {
        // 로그인 실패 시 응답
        return "/error/FormLoginSecurityError";
    }
}
