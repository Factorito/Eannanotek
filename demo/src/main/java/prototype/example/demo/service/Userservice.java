package prototype.example.demo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prototype.example.demo.Dto.MailDto;
import prototype.example.demo.entity.SiteUser;
import prototype.example.demo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class Userservice {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Autowired
    public Userservice(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

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

    @Transactional
    public void updateUser(String email, String phone, String password) {
        SiteUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPhone(phone);

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }

    @Transactional
    public void sendSecurityCode(String email) {
        SiteUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String code = UUID.randomUUID().toString().substring(0, 6);
        user.setSecurityCode(code);
        user.setSecurityCodeExpiry(LocalDateTime.now().plusMinutes(10)); // 10분 후 만료

        userRepository.save(user);

        MailDto mailDto = new MailDto();
        mailDto.setAddress(email);
        mailDto.setTitle("비교견적 웹사이트 보안 코드");
        mailDto.setMessage("보안 코드는 " + code + " 입니다.");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        mailSender.send(message);
    }
}