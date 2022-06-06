package com.grp4.gcash.mini.transactionservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class CashIn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mobileNumber;
    private Double cashIn;
    private Double balance;

    private LocalDateTime createdAt;


    @PrePersist
    public void setPreData() {
        createdAt = LocalDateTime.now();
    }

    public CashIn(Double cashIn) {
        this.cashIn = cashIn;
    }

    public CashIn() {
    }
}
