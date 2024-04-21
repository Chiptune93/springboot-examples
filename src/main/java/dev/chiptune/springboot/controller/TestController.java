package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.repo.TestUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestUserRepository testUserRepository;

    public TestController(TestUserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    @GetMapping("")
    public String test(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        printSessionInfo(session);
        return testUserRepository.findAll().toString();
    }

    public void printSessionInfo(HttpSession session) {
        // 세션에서 모든 속성 이름을 가져옵니다.
        Enumeration<String> attributeNames = session.getAttributeNames();

        // 속성 이름이 있는 동안 반복합니다.
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);

            // 콘솔에 속성 이름과 값을 출력합니다.
            System.out.println("Attribute Name - " + attributeName + ", Value - " + attributeValue);
        }
    }
}
