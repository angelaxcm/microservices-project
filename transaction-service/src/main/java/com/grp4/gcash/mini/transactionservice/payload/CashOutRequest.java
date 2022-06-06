package com.grp4.gcash.mini.transactionservice.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CashOutRequest {

    @NotBlank
    private String mobileNumber;

    @NotNull
    private Double cashOutAmount;
}
