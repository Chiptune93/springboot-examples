package dev.chiptune.springboot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "test_user_authlist")
public class AuthList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String authName;

    public AuthList(String userId, String authName) {
        this.userId = userId;
        this.authName = authName;
    }
}
