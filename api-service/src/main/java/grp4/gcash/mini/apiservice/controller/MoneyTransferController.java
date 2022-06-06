package grp4.gcash.mini.apiservice.controller;


import con.tbs.payload.MoneyTransferRequest;
import con.tbs.payload.MoneyTransferResponse;
import grp4.gcash.mini.apiservice.exceptions.MoneyTransferFailedException;
import org.springframework.beans.factory.annotation.Value;
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
    private final String moneyTransferServiceEndpoint;

    public MoneyTransferController(RestTemplate restTemplate, @Value("${money-transfer-service.endpoint}") String moneyTransferServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.moneyTransferServiceEndpoint = moneyTransferServiceEndpoint;
    }

    @PostMapping
    public MoneyTransferResponse moneyTransfer(@Valid @RequestBody MoneyTransferRequest request) throws MoneyTransferFailedException {
        //TODO
        ResponseEntity<MoneyTransferResponse> response = restTemplate.postForEntity(moneyTransferServiceEndpoint + "/money-transfer", request, MoneyTransferResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new MoneyTransferFailedException("Money Transfer failed.");
    }

    @ExceptionHandler(MoneyTransferFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMoneyTransferFailedException(MoneyTransferFailedException e) {
        return Map.of("error", e.getMessage());
    }
}
