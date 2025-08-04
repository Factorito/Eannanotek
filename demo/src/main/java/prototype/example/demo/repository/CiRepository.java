package prototype.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prototype.example.demo.entity.Ci;

public interface CiRepository extends JpaRepository<Ci, String> {

}
