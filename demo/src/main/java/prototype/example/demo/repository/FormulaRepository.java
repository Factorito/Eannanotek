package prototype.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prototype.example.demo.entity.Formula;


import java.util.List;
import java.util.Optional;

public interface FormulaRepository extends JpaRepository<Formula, Long> {
    Optional<Formula> findByName(String name);
}