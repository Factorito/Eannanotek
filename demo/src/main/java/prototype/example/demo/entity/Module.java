package prototype.example.demo.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "module")
@Data
public class Module {

    @Id
    private String name; // 예: "P2.5"

    private int width;
    private int height;

    @Column(name = "px_width")
    private int pxWidth;

    @Column(name = "px_height")
    private int pxHeight;

    @Column(name = "unit_price")
    private int unitPrice;

    @Column(name = "power_consumption")
    private Float powerConsumption;

    private Integer brightness;  // cd/m²
    private String smd;          // 예: SMD2121
    private Integer frequency;   // Hz

    @Column(name = "environment_type")
    private String environmentType;  // "옥내", "옥외"
}
