package con.tbs.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetails {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Double balance;
    private LocalDateTime createdAt;

    public UserDetails(String userId, String email, String firstName, String lastName, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public UserDetails(String userId, String email, String firstName, String lastName, Double balance, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public UserDetails() {
    }
}
