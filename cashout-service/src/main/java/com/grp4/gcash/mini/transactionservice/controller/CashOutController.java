package com.grp4.gcash.mini.transactionservice.controller;

import con.tbs.payload.*;
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
    private final String activityServiceEndpoint;

    public CashOutController(RestTemplate restTemplate, @Value("${wallet-service.endpoint}") String walletServiceEndpoint, @Value("${activity-service.endpoint}") String activityServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.walletServiceEndpoint = walletServiceEndpoint;
        this.activityServiceEndpoint = activityServiceEndpoint;
    }

    @PostMapping("cash-out")
    public CashOutResponse cashOut(@Valid @RequestBody CashOutRequest request) throws InsufficientBalanceException, TransactionException{
        Double balance = 0.0;

        LogActivity logActivity = new LogActivity();

        //Get user balance
        ResponseEntity<GetWalletResponse> userEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + request.getUserId(), GetWalletResponse.class);
        if (userEntity.getStatusCode().is2xxSuccessful()) {
            GetWalletResponse User = userEntity.getBody();
            balance = User.getBalance();
        }

        if(sufficientBalance(balance, request.getCashOutAmount())) {
            //Update user balance
            UpdateWalletRequest updateUserWallet = new UpdateWalletRequest(request.getUserId(), balance - request.getCashOutAmount());
            HttpEntity<UpdateWalletRequest> updateWalletHTTPEntity = new HttpEntity<>(updateUserWallet);
            ResponseEntity<Void> updateWalletEntity = restTemplate.exchange(walletServiceEndpoint + "/wallet/" + request.getUserId(), HttpMethod.PUT, updateWalletHTTPEntity, Void.class);
            if (updateWalletEntity.getStatusCode().is2xxSuccessful()) {
                CashOutResponse response =
                        new CashOutResponse(updateUserWallet.getUserId(), request.getCashOutAmount(), updateUserWallet.getBalance());

                logActivity.setAction(LogActivityActions.CASH_OUT_SUCCESSFUL.toString());
                logActivity.setInformation("userId: " + request.getUserId());
                logActivity.setIdentity("userId: " + request.getUserId());
                HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);

                return response;
            }

            logActivity.setAction(LogActivityActions.CASH_IN_FAILED.toString());
            logActivity.setInformation("userId: " + request.getUserId());
            logActivity.setIdentity("userId: " + request.getUserId());
            HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);

            throw new TransactionException("Something Went Wrong");

        }

        logActivity.setAction(LogActivityActions.CASH_IN_FAILED.toString());
        logActivity.setInformation("userId: " + request.getUserId());
        logActivity.setIdentity("userId: " + request.getUserId());
        HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);

        throw new InsufficientBalanceException("Insufficient Balance");
    }

        public boolean sufficientBalance(Double Balance, Double amount){
        return Balance >= amount;
    }
}
