package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.UserLoginRequest;
import con.tbs.payload.UserLoginResponse;
import grp4.gcash.mini.apiservice.exceptions.UserLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("login")
public class UserLoginController {
    private final RestTemplate restTemplate;
    private final String loginServiceEndpoint;

    public UserLoginController(RestTemplate restTemplate, @Value("${login-service.endpoint}") String loginServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.loginServiceEndpoint = loginServiceEndpoint;
    }

    @PostMapping()
    public UserLoginResponse login(@Valid @RequestBody UserLoginRequest request) throws UserLoginException {
        ResponseEntity<UserLoginResponse> responseEntity = restTemplate.postForEntity(loginServiceEndpoint + "/user/login", request, UserLoginResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            return responseEntity.getBody();
        }
        throw new UserLoginException("HTTP-" + responseEntity.getStatusCodeValue());
    }
}
