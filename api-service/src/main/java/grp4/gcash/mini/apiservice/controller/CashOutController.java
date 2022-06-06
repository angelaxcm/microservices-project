package grp4.gcash.mini.apiservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("cashin")
public class CashOutController {
    private final RestTemplate restTemplate;
    private final String cashOutServiceEndpoint;

    public CashOutController(RestTemplate restTemplate, @Value("${cashOut-service.endpoint}") String cashOutServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.cashOutServiceEndpoint = cashOutServiceEndpoint;
    }


}
