package dev.chiptune.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Users implements Serializable {
    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
}
