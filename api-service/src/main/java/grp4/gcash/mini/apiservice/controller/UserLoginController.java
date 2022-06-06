package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.UserLoginRequest;
import con.tbs.payload.UserLoginResponse;
import grp4.gcash.mini.apiservice.exceptions.MoneyTransferFailedException;
import grp4.gcash.mini.apiservice.exceptions.UserLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

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

    @ExceptionHandler(UserLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserLoginException(UserLoginException e) {
        return Map.of("error", e.getMessage());
    }
}
