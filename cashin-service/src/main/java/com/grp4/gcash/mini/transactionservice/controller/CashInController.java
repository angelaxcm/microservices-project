package com.grp4.gcash.mini.transactionservice.controller;

import con.tbs.payload.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("transaction")
public class CashInController {

    private final RestTemplate restTemplate;
    private final String walletServiceEndpoint;

    public CashInController(RestTemplate restTemplate, @Value("${wallet-service.endpoint}") String walletServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.walletServiceEndpoint = walletServiceEndpoint;
    }

    @PostMapping("cash-in")
    public CashInResponse cashIn(@Valid @RequestBody CashInRequest request) throws TransactionException, ChanneNotFoundException {
        Double Balance = 0.0;

        if (request.getChannel().equals("OTC") || request.getChannel().equals("TOPUP")){
            ResponseEntity<GetWalletResponse> UserEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + request.getMobileNumber(), GetWalletResponse.class);
            if (UserEntity.getStatusCode().is2xxSuccessful()) {
                GetWalletResponse User = UserEntity.getBody();
                Balance = User.getBalance();
            }

            UpdateWalletRequest updateUserWallet = new UpdateWalletRequest(request.getMobileNumber(), Balance + request.getCashInAmount());
            HttpEntity<UpdateWalletRequest> updateWalletHTTPEntity = new HttpEntity<>(updateUserWallet);
            ResponseEntity<Void> updateWalletEntity = restTemplate.exchange(walletServiceEndpoint + "/wallet/" + request.getMobileNumber(), HttpMethod.PUT, updateWalletHTTPEntity, Void.class);
            if (updateWalletEntity.getStatusCode().is2xxSuccessful()) {
                CashInResponse response =
                        new CashInResponse(updateUserWallet.getUserId(), request.getCashInAmount(), updateUserWallet.getBalance());

                return response;
            }
            throw new TransactionException("Something Went Wrong");
        }
        throw new ChanneNotFoundException("Wrong Channel Type");
    }
}
