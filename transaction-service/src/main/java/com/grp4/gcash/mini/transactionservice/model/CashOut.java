package com.grp4.gcash.mini.transactionservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class CashOut {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mobileNumber;
    private Double cashOut;
    private Double balance;

    private LocalDateTime createdAt;

    @PrePersist
    public void setPreData() {
        createdAt = LocalDateTime.now();
    }

    public CashOut(String mobileNumber, Double cashOut) {
        this.cashOut = cashOut;
    }

    public CashOut() {
    }
}
