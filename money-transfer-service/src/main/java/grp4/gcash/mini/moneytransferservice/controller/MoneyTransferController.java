package grp4.gcash.mini.moneytransferservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("money-transfer")
public class MoneyTransferController {
    private final RestTemplate restTemplate;
    private final String walletServiceEndpoint;
    private final String activityServiceEndpoint;

    public MoneyTransferController(RestTemplate restTemplate,
                                   @Value("${wallet-service.endpoint}") String walletServiceEndpoint,
                                   @Value("${activity-service.endpoint}") String activityServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.walletServiceEndpoint = walletServiceEndpoint;
        this.activityServiceEndpoint = activityServiceEndpoint;
    }

    @PostMapping
    public MoneyTransferResponse moneyTransfer(@Valid@RequestBody MoneyTransferRequest request) throws InsufficentBalanceException, MoneyTransferException{
        Double senderBalance = 0.0;
        Double receiverBalance = 0.0;
        LogActivity logActivity = new LogActivity();

        //Get Sender Wallet
        ResponseEntity<GetWalletResponse> senderEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + request.getSenderId(), GetWalletResponse.class);
        if (senderEntity.getStatusCode().is2xxSuccessful()) {
            GetWalletResponse receiver = senderEntity.getBody();
            senderBalance = receiver.getBalance();
        }

        if(sufficientBalance(senderBalance, request.getAmount())){
            //Get Receiver Wallet
            ResponseEntity<GetWalletResponse> receiverEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + request.getReceiverId(), GetWalletResponse.class);
            if (receiverEntity.getStatusCode().is2xxSuccessful()) {
                GetWalletResponse receiver = receiverEntity.getBody();
                receiverBalance = receiver.getBalance();
            }

            //Update Sender Wallet
            UpdateWalletRequest updateSenderRequest = new UpdateWalletRequest(request.getSenderId(), senderBalance - request.getAmount());
            HttpEntity<UpdateWalletRequest> updateSenderHTTPEntity = new HttpEntity<>(updateSenderRequest);
            ResponseEntity<Void> updateSenderEntity = restTemplate.exchange(walletServiceEndpoint + "/wallet/" + request.getSenderId(), HttpMethod.PUT, updateSenderHTTPEntity, Void.class);
            if (updateSenderEntity.getStatusCode().is2xxSuccessful()) {

                //Update Receiver Wallet
                UpdateWalletRequest updateReceiverRequest = new UpdateWalletRequest(request.getReceiverId(), receiverBalance + request.getAmount());
                HttpEntity<UpdateWalletRequest> updateReceiverHTTPEntity = new HttpEntity<>(updateReceiverRequest);
                ResponseEntity<Void> updateReceiverEntity = restTemplate.exchange(walletServiceEndpoint + "/wallet/" + request.getReceiverId(), HttpMethod.PUT, updateReceiverHTTPEntity, Void.class);
                if (updateReceiverEntity.getStatusCode().is2xxSuccessful()) {
                    MoneyTransferResponse response =
                            new MoneyTransferResponse(request.getSenderId(), request.getReceiverId(), request.getAmount(), request.getMessage());

                    logActivity.setAction("MONEY_TRANSFER_SUCCESSFUL");
                    logActivity.setInformation("receiverId: " + request.getReceiverId() + " amount: " + request.getAmount() + " message: " + request.getMessage());
                    logActivity.setIdentity("senderId: " + request.getSenderId());
                    HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
                    return response;
                }

                logActivity.setAction("MONEY_TRANSFER_FAILED");
                logActivity.setInformation("receiverId: " + request.getReceiverId() + " amount: " + request.getAmount() + " message: " + request.getMessage());
                logActivity.setIdentity("senderId: " + request.getSenderId());
                HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
                throw new MoneyTransferException("Something went wrong!");
            }
        }
        logActivity.setAction("MONEY_TRANSFER_FAILED");
        logActivity.setInformation("receiverId: " + request.getReceiverId() + " amount: " + request.getAmount() + " message: " + request.getMessage());
        logActivity.setIdentity("senderId: " + request.getSenderId());
        HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
        throw new InsufficentBalanceException("Insufficient Balance!");
    }

    public boolean sufficientBalance(Double senderBalance, Double amount){
        return senderBalance >= amount;
    }

    @ExceptionHandler(InsufficentBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInsufficientBalanceException(InsufficentBalanceException e) {
        return Map.of("error", e.getMessage());
    }
    @ExceptionHandler(MoneyTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMoneyTransferException(MoneyTransferException e) {
        return Map.of("error", e.getMessage());
    }
}
