package prototype.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "formula")
@Data
public class Formula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer formulaID;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expression;

    private String unit;

    private Timestamp lastUpdated;
}
