package prototype.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prototype.example.demo.entity.SiteUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, String> {
    Optional<SiteUser> findByEmailAndPassword(String Email, String password);
    Optional<SiteUser> findByEmail(String Email);
}

