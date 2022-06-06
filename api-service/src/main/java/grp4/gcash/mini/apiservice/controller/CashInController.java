package grp4.gcash.mini.apiservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("cashin")
public class CashInController {
    private final RestTemplate restTemplate;
    private final String cashInServiceEndpoint;

    public CashInController(RestTemplate restTemplate, @Value("${cashIn-service.endpoint}") String cashInServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.cashInServiceEndpoint = cashInServiceEndpoint;
    }
}