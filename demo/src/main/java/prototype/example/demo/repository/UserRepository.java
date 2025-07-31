package prototype.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prototype.example.demo.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}
