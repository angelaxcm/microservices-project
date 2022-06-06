package com.grp4.gcash.mini.transactionservice.controller;

import con.tbs.payload.CashOutRequest;
import con.tbs.payload.CashOutResponse;
import con.tbs.payload.GetWalletResponse;
import con.tbs.payload.UpdateWalletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("transaction")
public class CashOutController {

    private final RestTemplate restTemplate;
    private final String walletServiceEndpoint;

    public CashOutController(RestTemplate restTemplate, @Value("${wallet-service.endpoint}") String walletServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.walletServiceEndpoint = walletServiceEndpoint;
    }

    @PostMapping("cash-out")
    public CashOutResponse cashOut(@Valid @RequestBody CashOutRequest request) throws InsufficientBalanceException, TransactionException{
        Double Balance = 0.0;

        if(sufficientBalance(Balance, request.getCashOutAmount())) {

            ResponseEntity<GetWalletResponse> UserEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + request.getMobileNumber(), GetWalletResponse.class);
            if (UserEntity.getStatusCode().is2xxSuccessful()) {
                GetWalletResponse User = UserEntity.getBody();
                Balance = User.getBalance();
            }

            UpdateWalletRequest updateUserWallet = new UpdateWalletRequest(request.getMobileNumber(), Balance - request.getCashOutAmount());
            HttpEntity<UpdateWalletRequest> updateWalletHTTPEntity = new HttpEntity<>(updateUserWallet);
            ResponseEntity<Void> updateWalletEntity = restTemplate.exchange(walletServiceEndpoint + "/wallet/" + request.getMobileNumber(), HttpMethod.PUT, updateWalletHTTPEntity, Void.class);
            if (updateWalletEntity.getStatusCode().is2xxSuccessful()) {
                CashOutResponse response =
                        new CashOutResponse(updateUserWallet.getUserId(), request.getCashOutAmount(), updateUserWallet.getBalance());

                return response;
            }
            throw new TransactionException("Something Went Wrong");
        }
        throw new InsufficientBalanceException("Insufficient Balance");
    }

        public boolean sufficientBalance(Double Balance, Double amount){
        return Balance >= amount;
    }
}
