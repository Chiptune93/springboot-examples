package dev.chiptune.springboot.repo;

import dev.chiptune.springboot.entity.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestUserRepository extends JpaRepository<TestUser, Long> {
    TestUser findByNameAndPassword(String username, String password);
}
