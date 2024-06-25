package dev.chiptune.springboot.security.authority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorities {
    String userId;
    List<String> userTypeRole;
    List<String> authorities;

    @Override
    public String toString() {
        return "UserAuthorities{" +
                "userId='" + userId + '\'' +
                ", userTypeRole=" + userTypeRole +
                ", authorities=" + authorities +
                '}';
    }
}
