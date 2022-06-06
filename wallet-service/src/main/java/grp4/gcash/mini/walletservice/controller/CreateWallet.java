package grp4.gcash.mini.walletservice.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateWallet {
    @NotBlank
    private String userId;
}
