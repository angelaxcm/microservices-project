package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.UserRegistrationRequest;
import con.tbs.payload.UserRegistrationResponse;
import grp4.gcash.mini.apiservice.exceptions.UserRegistrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("register")
public class UserRegistrationController {
    private final RestTemplate restTemplate;
    private final String userServiceEndpoint;

    public UserRegistrationController(RestTemplate restTemplate, @Value("${user-service.endpoint}") String userServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.userServiceEndpoint = userServiceEndpoint;
    }

    @PostMapping
    public UserRegistrationResponse register(@Valid @RequestBody UserRegistrationRequest request) throws UserRegistrationException {
        ResponseEntity<UserRegistrationResponse> responseEntity = restTemplate.postForEntity(userServiceEndpoint + "/user", request, UserRegistrationResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            return responseEntity.getBody();
        }
        throw new UserRegistrationException("HTTP-" + responseEntity.getStatusCodeValue());
    }
}
