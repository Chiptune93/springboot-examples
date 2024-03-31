package dev.chiptune.springboot.repo;

import dev.chiptune.springboot.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Users findByUsernameAndPassword(String username, String password);
}
