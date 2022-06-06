package con.tbs.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetUserResponse {
    private String userId;

    private String firstName;
    private String lastName;
    private String middleName;

    private String email;
    private String password;

    private Double balance;

    private LocalDateTime lastLoggedIn;
    private LocalDateTime lastUpdated;
    private LocalDateTime dateCreated;

    public GetUserResponse(String userId, String firstName, String lastName, String middleName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
    }

    public GetUserResponse(String userId, String firstName, String lastName, String middleName, String email, String password, Double balance) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public GetUserResponse() {
    }
}
