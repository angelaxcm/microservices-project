package com.grp4.gcash.mini.transactionservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
public class CashInResponse {


    private String mobileNumber;
    private Double cashInAmount;
    private Double balance;

    public CashInResponse(String mobileNumber, Double cashInAmount, Double balance) {
        this.mobileNumber = mobileNumber;
        this.cashInAmount = cashInAmount;
        this.balance = balance;
    }

    public CashInResponse() {
    }
}
