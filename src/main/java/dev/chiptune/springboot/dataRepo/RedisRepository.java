package dev.chiptune.springboot.dataRepo;

import dev.chiptune.springboot.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<TestEntity, String> {
}
