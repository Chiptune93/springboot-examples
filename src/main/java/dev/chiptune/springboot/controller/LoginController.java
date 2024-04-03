package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.config.domain.CustomUserDetails;
import dev.chiptune.springboot.config.token.JwtTokenProvider;
import dev.chiptune.springboot.entity.Users;
import dev.chiptune.springboot.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
class LoginController {

    private final static Logger logger = Logger.getLogger(LoginController.class.getName());

    private final
    UsersService usersService;

    private final JwtTokenProvider jwtTokenProvider;

    // ---------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------

    @PostMapping("/getToken")
    public CustomUserDetails getToken(@RequestBody Users users) throws Exception {
        logger.info(users.toString());
        try {
            // 인증 정보 체크
            Users findUser = usersService.findUserForLogin(users.getUsername(), users.getPassword());
            if (findUser != null) {
                CustomUserDetails customUserDetails = new CustomUserDetails(findUser);
                // 액세스 토큰 생성 및 세팅
                customUserDetails.setAccessToken(jwtTokenProvider.generateAccessToken(customUserDetails));
                // 리프레시 토큰 생성 및 세팅
                customUserDetails.setRefreshToken(jwtTokenProvider.generateRefreshToken());
                return customUserDetails;
            } else {
                // 에러 처리
                throw new Exception("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("인증 과정에 문제가 발생하였습니다. 사용자 아이디 또는 암호를 확인하세요.");
        }
    }

    @PostMapping("/refreshAccessToken")
    public CustomUserDetails refreshAccessToken(@RequestBody CustomUserDetails customUserDetails) throws Exception {
        try {
            if (jwtTokenProvider.validateRefreshToken(customUserDetails.getAccessToken(), customUserDetails.getRefreshToken())) { // 리프레시 토큰이 유효한 경우.
                // 새 액세스 토큰과 리프레시 토큰 발급
                CustomUserDetails result = new CustomUserDetails();
                result.setAccessToken(jwtTokenProvider.generateNewAccessToken(customUserDetails.getAccessToken()));
                result.setRefreshToken(jwtTokenProvider.generateRefreshToken());

                logger.info("--- New Token Info ---");
                logger.info("result user id : " + customUserDetails.getUsername());
                logger.info("result access token : " + result.getAccessToken());
                logger.info("result refresh token : " + result.getRefreshToken());

                return result;
            } else { // 리프레시 토큰이 유효하지 않은 경우.
                // 에러 처리
                throw new Exception("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("인증 과정에 문제가 발생하였습니다. 사용자 아이디 또는 암호를 확인하세요.");
        }
    }


}
