package dev.chiptune.springboot.repo;

import dev.chiptune.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    User findByUserIdAndUserType(String userId, String userType);
    User findByUserIdAndUserPw(String userId, String userPw);
    // User findByUserIdAndUserPwAndUserType(String userId, String userPw, String userType);
}
