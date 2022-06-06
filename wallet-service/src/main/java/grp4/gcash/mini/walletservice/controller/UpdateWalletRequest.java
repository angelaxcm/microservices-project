package grp4.gcash.mini.walletservice.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateWalletRequest {
    @NotBlank
    private String userId;
    @NotNull
    @Positive
    private Double balance;
}