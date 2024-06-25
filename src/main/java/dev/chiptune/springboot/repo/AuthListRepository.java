package dev.chiptune.springboot.repo;

import dev.chiptune.springboot.entity.AuthList;
import dev.chiptune.springboot.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthListRepository extends JpaRepository<AuthList, Long> {
    List<AuthList> findAllByUserId(String userId);
}
