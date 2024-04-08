package dev.chiptune.springboot;

import dev.chiptune.springboot.entity.TestUser;
import dev.chiptune.springboot.repo.TestUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(TestUserRepository userRepository) {
		return args -> {
			// 샘플 데이터 삽입
			userRepository.save(new TestUser("Alice", "alice@example.com"));
			userRepository.save(new TestUser("Bob", "bob@example.com"));
			userRepository.save(new TestUser("Charlie", "charlie@example.com"));
			userRepository.save(new TestUser("David", "david@example.com"));
			userRepository.save(new TestUser("Eve", "eve@example.com"));
			userRepository.save(new TestUser("Frank", "frank@example.com"));
			userRepository.save(new TestUser("Grace", "grace@example.com"));
			userRepository.save(new TestUser("Hannah", "hannah@example.com"));
			userRepository.save(new TestUser("Ivy", "ivy@example.com"));
			userRepository.save(new TestUser("Jack", "jack@example.com"));
		};
	}

}
