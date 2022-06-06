package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.UserRegistrationRequest;
import con.tbs.payload.UserRegistrationResponse;
import grp4.gcash.mini.apiservice.exceptions.UserLoginException;
import grp4.gcash.mini.apiservice.exceptions.UserRegistrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

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
        ResponseEntity<UserRegistrationResponse> responseEntity = restTemplate.postForEntity(userServiceEndpoint + "/user/register", request, UserRegistrationResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            return responseEntity.getBody();
        }
        throw new UserRegistrationException("HTTP-" + responseEntity.getStatusCodeValue());
    }

    @ExceptionHandler(UserRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserRegistrationException(UserRegistrationException e) {
        return Map.of("error", e.getMessage());
    }
}
