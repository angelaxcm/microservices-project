package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.CashInRequest;
import con.tbs.payload.CashInResponse;
import con.tbs.payload.CashOutRequest;
import con.tbs.payload.CashOutResponse;
import grp4.gcash.mini.apiservice.exceptions.CashInServiceException;
import grp4.gcash.mini.apiservice.exceptions.CashOutServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

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
    public CashOutResponse cashOut(@Valid @RequestBody CashOutRequest request) throws CashOutServiceException {
        ResponseEntity<CashOutResponse> responseEntity = restTemplate.postForEntity(cashOutServiceEndpoint + "/transaction/cash-out", request, CashOutResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            return responseEntity.getBody();
        }
        throw new CashOutServiceException("HTTP-" + responseEntity.getStatusCodeValue());
    }
    @ExceptionHandler(CashOutServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleCashOutServiceException(CashOutServiceException e) {
        return Map.of("error", e.getMessage());
    }
}
