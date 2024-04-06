package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.service.RedisService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    final
    RedisService redisService;

    public TestController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/redis")
    public String redis() {
        redisService.setStringValue("test", "testStringValue");
        return redisService.getStringValue("test");
    }
}
