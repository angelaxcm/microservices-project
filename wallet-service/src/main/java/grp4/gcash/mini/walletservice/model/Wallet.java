package grp4.gcash.mini.walletservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@Entity
public class Wallet {
    @Id
    String userId;

    Double balance;

    LocalDateTime lastUpdated;

    @PrePersist
    public void setPreData(){
        LocalDateTime now = LocalDateTime.now();
        lastUpdated = now;
        balance = 0.0;
    }

    @PreUpdate
    public void setUpdateTimeStamp(){
        lastUpdated = LocalDateTime.now();
    }

    public Wallet(String userId){
        this.userId = userId;
    }

    public Wallet(){
    }
}
