package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.CashInRequest;
import con.tbs.payload.CashInResponse;
import grp4.gcash.mini.apiservice.exceptions.CashInServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("cash-in")
public class CashInController {
    private final RestTemplate restTemplate;
    private final String cashInServiceEndpoint;

    public CashInController(RestTemplate restTemplate, @Value("${cashIn-service.endpoint}") String cashInServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.cashInServiceEndpoint = cashInServiceEndpoint;
    }

    @PostMapping
    public CashInResponse cashIn(@Valid @RequestBody CashInRequest request) throws CashInServiceException {
        ResponseEntity<CashInResponse> responseEntity = restTemplate.postForEntity(cashInServiceEndpoint + "/transaction/cash-in", request, CashInResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            return responseEntity.getBody();
        }
        throw new CashInServiceException("HTTP-" + responseEntity.getStatusCodeValue());
    }


}