package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.CashInRequest;
import con.tbs.payload.CashInResponse;
import con.tbs.payload.CashOutRequest;
import grp4.gcash.mini.apiservice.exceptions.CashOutServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("cash-out")
public class CashOutController {
    private final RestTemplate restTemplate;
    private final String cashOutServiceEndpoint;

    public CashOutController(RestTemplate restTemplate, @Value("${cashOut-service.endpoint}") String cashOutServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.cashOutServiceEndpoint = cashOutServiceEndpoint;
    }

    @PostMapping
    public CashOutRequest cashIn(@Valid @RequestBody CashOutRequest request) throws CashOutServiceException {
        ResponseEntity<CashOutRequest> responseEntity = restTemplate.postForEntity(cashOutServiceEndpoint + "/transaction/cash-out", request, CashOutRequest.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            return responseEntity.getBody();
        }
        throw new CashOutServiceException("HTTP-" + responseEntity.getStatusCodeValue());
    }
}
