package con.tbs.payload;

import lombok.Data;

@Data
public class UserRegistrationResponse {
    private String userId;

    public UserRegistrationResponse(String mobileNumber) {
        this.userId = mobileNumber;
    }

    public UserRegistrationResponse() {
    }
}

