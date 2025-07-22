package prototype.example.demo.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "formula")
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

    // Getter, Setter
    public Integer getFormulaID() { return formulaID; }
    public void setFormulaID(Integer formulaID) { this.formulaID = formulaID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
} 