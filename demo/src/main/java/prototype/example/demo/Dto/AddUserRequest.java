package prototype.example.demo.Dto;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    @NotEmpty(message = "사용자Email은 필수항목입니다.")
    @Email
    private String email;

    @Size(min = 8, max = 25)
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "전화번호는 필수항목입니다..")
    private String phone;

}