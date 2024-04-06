package dev.chiptune.springboot.controller;

import dev.chiptune.springboot.dataRepo.RedisRepository;
import dev.chiptune.springboot.entity.TestEntity;
import dev.chiptune.springboot.service.RedisService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    final RedisService redisService;

    final RedisRepository redisRepository;

    public TestController(RedisService redisService, RedisRepository redisRepository) {
        this.redisService = redisService;
        this.redisRepository = redisRepository;
    }

    @PostMapping("/redis")
    public String redis() {
        redisService.setStringValue("test", "testStringValue");
        return redisService.getStringValue("test");
    }

    @PostMapping("/redis-data-jdbc")
    public Iterable<TestEntity> redisDataJdbc() {
        redisRepository.save(new TestEntity("1", "test"));
        return redisRepository.findAll();
    }
}
