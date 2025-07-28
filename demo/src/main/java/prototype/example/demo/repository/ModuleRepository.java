package prototype.example.demo.repository;

import prototype.example.demo.entity.module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<module, String> {
    Optional<module> findByName(String name);
}
