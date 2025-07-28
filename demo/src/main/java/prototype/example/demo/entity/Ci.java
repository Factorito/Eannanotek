package prototype.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ci")
@IdClass(CiId.class)
@Data
public class Ci {

    @Id
    @Column(length = 255)
    private String email2;

    @Id
    private int installDay;

    private String installReigon;
    private String caseType;
    private String module;
    private String installEnviroment;
    private String field;
    private int width;
    private int height;

    private String displaySize;
    private Float actualWidth;
    private Float actualHeight;
    private Integer quantity;
    private String aspectRatio;
    private Integer pixelWidth;
    private Integer pixelHeight;
    private Integer estimatedPrice;
}
