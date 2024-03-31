package dev.chiptune.springboot.service;

import dev.chiptune.springboot.entity.Users;
import dev.chiptune.springboot.repo.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository userRepository;

    @Autowired
    public UsersService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public Users findByUsernameAndPassword(String username, String password) {
        System.out.println("username -> " + username);
        System.out.println("password -> " + password);
        return userRepository.findByUsernameAndPassword(username, password);
    }
}

