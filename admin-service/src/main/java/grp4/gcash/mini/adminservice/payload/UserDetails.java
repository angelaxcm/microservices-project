package grp4.gcash.mini.adminservice.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetails {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;

    public UserDetails(String userId, String email, String firstName, String lastName, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public UserDetails() {
    }
}
