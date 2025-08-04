package prototype.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prototype.example.demo.entity.SiteUser;
import prototype.example.demo.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SiteUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 사용자 권한 설정 (ADMIN 여부에 따라 ROLE_ADMIN 또는 ROLE_USER)
        SimpleGrantedAuthority authority = user.isAA() ?
                new SimpleGrantedAuthority("ROLE_ADMIN") :
                new SimpleGrantedAuthority("ROLE_USER");



        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}

