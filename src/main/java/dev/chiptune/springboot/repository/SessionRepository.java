package dev.chiptune.springboot.repository;

import dev.chiptune.springboot.data.SessionData;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<SessionData, String> {
    SessionData findBySessionId(String sessionId);
}
