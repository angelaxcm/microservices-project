package com.grp4.gcash.mini.transactionservice.payload;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
public class CashOutResponse {

    private String mobileNumber;
    private Double cashOutAmount;
    private Double balance;

    public CashOutResponse(String mobileNumber, Double cashOutAmount, Double balance) {
        this.mobileNumber = mobileNumber;
        this.balance = balance;
        this.cashOutAmount = cashOutAmount;
    }

    public CashOutResponse() {
    }
}
