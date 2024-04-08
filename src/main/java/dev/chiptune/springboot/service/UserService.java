package dev.chiptune.springboot.service;

import dev.chiptune.springboot.entity.Users;
import dev.chiptune.springboot.repo.UserRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    final
    UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Cacheable(
            value = "UserCache",
            key = "#id"
    )
    public Users getUser(Long id) {
        return userRepo.findById(id).get();
    }

}
