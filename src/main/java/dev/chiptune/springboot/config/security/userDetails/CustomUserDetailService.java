package dev.chiptune.springboot.config.security.userDetails;

import dev.chiptune.springboot.entity.Users;
import dev.chiptune.springboot.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("--------------------------------");
        System.out.println("! LoadUserByUserName !");
        System.out.println("--------------------------------");
        Users users = usersRepository.findByUsername(username);
        System.out.println("--------------------------------");
        System.out.println("! users : " + users.toString());
        System.out.println("--------------------------------");
        return new CustomUserDetails(users);
    }
}
