package dev.chiptune.springboot.security.userDetails;

import dev.chiptune.springboot.entity.AuthList;
import dev.chiptune.springboot.entity.User;
import dev.chiptune.springboot.entity.UserRole;
import dev.chiptune.springboot.repo.AuthListRepository;
import dev.chiptune.springboot.repo.RoleRepository;
import dev.chiptune.springboot.repo.UserRepository;
import dev.chiptune.springboot.security.authority.UserAuthorities;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthListRepository authListRepository;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, AuthListRepository authListRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authListRepository = authListRepository;
    }

    public User attempLogin(String userId, String userPw) {
        return userRepository.findByUserIdAndUserPw(userId, userPw);
    }

    // 사용자의 권한을 조회한다.
    public UserAuthorities getUserAuthorities(String userId) {
        UserAuthorities userAuthorities = new UserAuthorities();
        userAuthorities.setUserId(userId);
        // Role을 조회하여 저장한다.
        userAuthorities.setUserTypeRole(
                roleRepository.findAllByUserId(userId).stream().map(UserRole::getRoleName)
                        .collect(Collectors.toList())
        );
        userAuthorities.setAuthorities(
                authListRepository.findAllByUserId(userId).stream().map(AuthList::getAuthName)
                        .collect(Collectors.toList())
        );
        System.out.println("------------------------------");
        System.out.println("getUserAuthorities : " + userAuthorities);
        System.out.println("------------------------------");
        return userAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("------------------------------");
        System.out.println("loadUserByUsername Try username : " + username);
        System.out.println("------------------------------");
        return new CustomUserDetails(userRepository.findByUserId(username));
    }
}
