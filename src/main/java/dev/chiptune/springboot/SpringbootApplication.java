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
            userRepository.save(new TestUser("User 1", "1234", "user1@example.com"));
            userRepository.save(new TestUser("User 2", "1234", "user2@example.com"));
            userRepository.save(new TestUser("User 3", "1234", "user3@example.com"));
            userRepository.save(new TestUser("User 4", "1234", "user4@example.com"));
            userRepository.save(new TestUser("User 5", "1234", "user5@example.com"));
            userRepository.save(new TestUser("User 6", "1234", "user6@example.com"));
            userRepository.save(new TestUser("User 7", "1234", "user7@example.com"));
            userRepository.save(new TestUser("User 8", "1234", "user8@example.com"));
            userRepository.save(new TestUser("User 9", "1234", "user9@example.com"));
            userRepository.save(new TestUser("User 10", "1234", "user10@example.com"));
            userRepository.save(new TestUser("User 11", "1234", "user11@example.com"));
            userRepository.save(new TestUser("User 12", "1234", "user12@example.com"));
            userRepository.save(new TestUser("User 13", "1234", "user13@example.com"));
            userRepository.save(new TestUser("User 14", "1234", "user14@example.com"));
            userRepository.save(new TestUser("User 15", "1234", "user15@example.com"));
            userRepository.save(new TestUser("User 16", "1234", "user16@example.com"));
            userRepository.save(new TestUser("User 17", "1234", "user17@example.com"));
            userRepository.save(new TestUser("User 18", "1234", "user18@example.com"));
            userRepository.save(new TestUser("User 19", "1234", "user19@example.com"));
            userRepository.save(new TestUser("User 20", "1234", "user20@example.com"));
            userRepository.save(new TestUser("User 21", "1234", "user21@example.com"));
            userRepository.save(new TestUser("User 22", "1234", "user22@example.com"));
            userRepository.save(new TestUser("User 23", "1234", "user23@example.com"));
            userRepository.save(new TestUser("User 24", "1234", "user24@example.com"));
            userRepository.save(new TestUser("User 25", "1234", "user25@example.com"));
            userRepository.save(new TestUser("User 26", "1234", "user26@example.com"));
            userRepository.save(new TestUser("User 27", "1234", "user27@example.com"));
            userRepository.save(new TestUser("User 28", "1234", "user28@example.com"));
            userRepository.save(new TestUser("User 29", "1234", "user29@example.com"));
            userRepository.save(new TestUser("User 30", "1234", "user30@example.com"));
            userRepository.save(new TestUser("User 31", "1234", "user31@example.com"));
            userRepository.save(new TestUser("User 32", "1234", "user32@example.com"));
            userRepository.save(new TestUser("User 33", "1234", "user33@example.com"));
            userRepository.save(new TestUser("User 34", "1234", "user34@example.com"));
            userRepository.save(new TestUser("User 35", "1234", "user35@example.com"));
            userRepository.save(new TestUser("User 36", "1234", "user36@example.com"));
            userRepository.save(new TestUser("User 37", "1234", "user37@example.com"));
            userRepository.save(new TestUser("User 38", "1234", "user38@example.com"));
            userRepository.save(new TestUser("User 39", "1234", "user39@example.com"));
            userRepository.save(new TestUser("User 40", "1234", "user40@example.com"));
            userRepository.save(new TestUser("User 41", "1234", "user41@example.com"));
            userRepository.save(new TestUser("User 42", "1234", "user42@example.com"));
            userRepository.save(new TestUser("User 43", "1234", "user43@example.com"));
            userRepository.save(new TestUser("User 44", "1234", "user44@example.com"));
            userRepository.save(new TestUser("User 45", "1234", "user45@example.com"));
            userRepository.save(new TestUser("User 46", "1234", "user46@example.com"));
            userRepository.save(new TestUser("User 47", "1234", "user47@example.com"));
            userRepository.save(new TestUser("User 48", "1234", "user48@example.com"));
            userRepository.save(new TestUser("User 49", "1234", "user49@example.com"));
            userRepository.save(new TestUser("User 50", "1234", "user50@example.com"));
            userRepository.save(new TestUser("User 51", "1234", "user51@example.com"));
            userRepository.save(new TestUser("User 52", "1234", "user52@example.com"));
            userRepository.save(new TestUser("User 53", "1234", "user53@example.com"));
            userRepository.save(new TestUser("User 54", "1234", "user54@example.com"));
            userRepository.save(new TestUser("User 55", "1234", "user55@example.com"));
            userRepository.save(new TestUser("User 56", "1234", "user56@example.com"));
            userRepository.save(new TestUser("User 57", "1234", "user57@example.com"));
            userRepository.save(new TestUser("User 58", "1234", "user58@example.com"));
            userRepository.save(new TestUser("User 59", "1234", "user59@example.com"));
            userRepository.save(new TestUser("User 60", "1234", "user60@example.com"));
            userRepository.save(new TestUser("User 61", "1234", "user61@example.com"));
            userRepository.save(new TestUser("User 62", "1234", "user62@example.com"));
            userRepository.save(new TestUser("User 63", "1234", "user63@example.com"));
            userRepository.save(new TestUser("User 64", "1234", "user64@example.com"));
            userRepository.save(new TestUser("User 65", "1234", "user65@example.com"));
            userRepository.save(new TestUser("User 66", "1234", "user66@example.com"));
            userRepository.save(new TestUser("User 67", "1234", "user67@example.com"));
            userRepository.save(new TestUser("User 68", "1234", "user68@example.com"));
            userRepository.save(new TestUser("User 69", "1234", "user69@example.com"));
            userRepository.save(new TestUser("User 70", "1234", "user70@example.com"));
            userRepository.save(new TestUser("User 71", "1234", "user71@example.com"));
            userRepository.save(new TestUser("User 72", "1234", "user72@example.com"));
            userRepository.save(new TestUser("User 73", "1234", "user73@example.com"));
            userRepository.save(new TestUser("User 74", "1234", "user74@example.com"));
            userRepository.save(new TestUser("User 75", "1234", "user75@example.com"));
            userRepository.save(new TestUser("User 76", "1234", "user76@example.com"));
            userRepository.save(new TestUser("User 77", "1234", "user77@example.com"));
            userRepository.save(new TestUser("User 78", "1234", "user78@example.com"));
            userRepository.save(new TestUser("User 79", "1234", "user79@example.com"));
            userRepository.save(new TestUser("User 80", "1234", "user80@example.com"));
            userRepository.save(new TestUser("User 81", "1234", "user81@example.com"));
            userRepository.save(new TestUser("User 82", "1234", "user82@example.com"));
            userRepository.save(new TestUser("User 83", "1234", "user83@example.com"));
            userRepository.save(new TestUser("User 84", "1234", "user84@example.com"));
            userRepository.save(new TestUser("User 85", "1234", "user85@example.com"));
            userRepository.save(new TestUser("User 86", "1234", "user86@example.com"));
            userRepository.save(new TestUser("User 87", "1234", "user87@example.com"));
            userRepository.save(new TestUser("User 88", "1234", "user88@example.com"));
            userRepository.save(new TestUser("User 89", "1234", "user89@example.com"));
            userRepository.save(new TestUser("User 90", "1234", "user90@example.com"));
            userRepository.save(new TestUser("User 91", "1234", "user91@example.com"));
            userRepository.save(new TestUser("User 92", "1234", "user92@example.com"));
            userRepository.save(new TestUser("User 93", "1234", "user93@example.com"));
            userRepository.save(new TestUser("User 94", "1234", "user94@example.com"));
            userRepository.save(new TestUser("User 95", "1234", "user95@example.com"));
            userRepository.save(new TestUser("User 96", "1234", "user96@example.com"));
            userRepository.save(new TestUser("User 97", "1234", "user97@example.com"));
            userRepository.save(new TestUser("User 98", "1234", "user98@example.com"));
            userRepository.save(new TestUser("User 99", "1234", "user99@example.com"));
            userRepository.save(new TestUser("User 100", "1234", "user100@example.com"));
        };
    }

}
