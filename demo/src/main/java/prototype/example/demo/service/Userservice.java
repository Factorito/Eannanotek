package prototype.example.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prototype.example.demo.Dto.AddUserRequest;
import prototype.example.demo.entity.SiteUser;
import prototype.example.demo.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class Userservice {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 정보를 받아 User 엔티티를 생성하고 저장하는 메서드
    @Transactional
    public SiteUser create(String email, String password, String phone) {
        // 이미 존재하는 이메일인지 확인하는 로직은 컨트롤러에 유지하거나,
        // 이곳으로 옮겨 비즈니스 로직을 통합할 수 있습니다.
        SiteUser siteUser = new SiteUser();
        if (userRepository.findByEmail(email).isPresent() ){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));
        siteUser.setPhone(phone);
        siteUser.setAA(false);
        this.userRepository.save(siteUser);
        return siteUser;
    }
}