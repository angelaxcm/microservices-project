package grp4.gcash.mini.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
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

    @PrePersist
    public void setPreData() {
        Double balance = 0.0;
        LocalDateTime now = LocalDateTime.now();
        dateCreated = now;
        lastLoggedIn = now;
    }

    @PreUpdate
    public void setUpdateTimeStamp(){
        lastUpdated = LocalDateTime.now();
    }

    public User(String userId, String firstName, String lastName, String middleName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
    }
}
