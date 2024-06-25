package dev.chiptune.springboot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "test_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String userPw;
    private String userType;
    private String userName;
    private String userEmail;

    public User(String userId, String userPw, String userType, String userName, String userEmail) {
        this.userId = userId;
        this.userPw = userPw;
        this.userType = userType;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
