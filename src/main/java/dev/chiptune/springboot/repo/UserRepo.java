package dev.chiptune.springboot.repo;

import dev.chiptune.springboot.entity.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<Users, Long> {
}
