package con.tbs.payload;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    public UserLoginRequest() {
    }
}

