package com.grp4.gcash.mini.transactionservice.controller;

import com.grp4.gcash.mini.transactionservice.exceptions.ChannelNotFoundException;
import com.grp4.gcash.mini.transactionservice.exceptions.TransactionException;
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
    private final String activityServiceEndpoint;

    public CashInController(RestTemplate restTemplate, @Value("${wallet-service.endpoint}") String walletServiceEndpoint, @Value("${activity-service.endpoint}") String activityServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.walletServiceEndpoint = walletServiceEndpoint;
        this.activityServiceEndpoint = activityServiceEndpoint;
    }

    @PostMapping("cash-in")
    public CashInResponse cashIn(@Valid @RequestBody CashInRequest request) throws TransactionException, ChannelNotFoundException {
        Double balance = 0.0;

        LogActivity logActivity = new LogActivity();

        //Get user balance
        if (request.getChannel().equals("OTC") || request.getChannel().equals("TOPUP")) {
            ResponseEntity<GetWalletResponse> userEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + request.getUserId(), GetWalletResponse.class);
            if (userEntity.getStatusCode().is2xxSuccessful()) {
                GetWalletResponse User = userEntity.getBody();
                balance = User.getBalance();
            }

            //Update user balance
            UpdateWalletRequest updateUserWallet = new UpdateWalletRequest(request.getUserId(), balance + request.getCashInAmount());
            HttpEntity<UpdateWalletRequest> updateWalletHTTPEntity = new HttpEntity<>(updateUserWallet);
            ResponseEntity<Void> updateWalletEntity = restTemplate.exchange(walletServiceEndpoint + "/wallet/" + request.getUserId(), HttpMethod.PUT, updateWalletHTTPEntity, Void.class);
            if (updateWalletEntity.getStatusCode().is2xxSuccessful()) {
                CashInResponse response =
                        new CashInResponse(updateUserWallet.getUserId(), request.getCashInAmount(), updateUserWallet.getBalance());

                logActivity.setAction(LogActivityActions.CASH_IN_SUCCESSFUL.toString());
                logActivity.setInformation("userId: " + request.getUserId() +  "amount: " + request.getCashInAmount() + "channel: " + request.getChannel());
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

        throw new ChannelNotFoundException("Wrong Channel Type");
    }
}
