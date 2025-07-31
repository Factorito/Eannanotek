package prototype.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prototype.example.demo.entity.User;
import prototype.example.demo.repository.UserRepository;
/*
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());

        // 비밀번호를 BCrypt로 인코딩하여 저장
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }
}

*/
