package dev.chiptune.springboot.repo;

import dev.chiptune.springboot.entity.User;
import dev.chiptune.springboot.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUserId(String userId);
}
