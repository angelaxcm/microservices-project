package grp4.gcash.mini.activityservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String action;
    @Column(columnDefinition = "TEXT")
    private String information;
    private String identity;

    private LocalDateTime createdAt;

    @PrePersist
    public void setPreData() {
        createdAt = LocalDateTime.now();
    }

    public Activity(String action, String information, String identity) {
        this.action = action;
        this.information = information;
        this.identity = identity;
    }

    public Activity() {
    }
}