package dev.chiptune.springboot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "test_user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String roleName;

    public UserRole(String userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }
}
