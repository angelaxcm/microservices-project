package com.grp4.gcash.mini.transactionservice.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CashInRequest {

    @NotBlank
    private String mobileNumber;

    @NotNull
    private Double cashInAmount;
}
