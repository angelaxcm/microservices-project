package grp4.gcash.mini.loginservice.controller;

import con.tbs.payload.*;
import grp4.gcash.mini.loginservice.exception.UserLoginException;
import grp4.gcash.mini.loginservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final String userServiceEndpoint;
    private final String activityServiceEndpoint;

    public LoginController(RestTemplate restTemplate, PasswordEncoder passwordEncoder, @Value("${user-service.endpoint}") String userServiceEndpoint, @Value("${activity-service.endpoint}")String activityServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userServiceEndpoint = userServiceEndpoint;
        this.activityServiceEndpoint = activityServiceEndpoint;
    }

    @PostMapping()
    public UserLoginResponse login(@Valid @RequestBody UserLoginRequest request) throws UserNotFoundException, UserLoginException {

        LogActivity logActivity = new LogActivity();
        GetUserResponse user = getUser(request.getUserId());
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            ResponseEntity<Void> userEntity = restTemplate.postForEntity(userServiceEndpoint + "/user/login/" + request.getUserId(),null, Void.class);
            if(userEntity.getStatusCode().is2xxSuccessful())
            {
                logActivity.setAction(LogActivityActions.AUTHENTICATION_SUCCESSFUL.toString());
                logActivity.setInformation("userId: " + request.getUserId());
                logActivity.setIdentity("userId: " + request.getUserId());
                HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);

                return new UserLoginResponse(user.getUserId(), LocalDateTime.now());
            }
            logActivity.setAction(LogActivityActions.AUTHENTICATION_FAILED.toString());
            logActivity.setInformation("userId: " + request.getUserId());
            logActivity.setIdentity("userId: " + request.getUserId());
            HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);

            throw new UserLoginException("HTTP-" + userEntity.getStatusCodeValue());
        }
        logActivity.setAction(LogActivityActions.AUTHENTICATION_FAILED.toString());
        logActivity.setInformation("userId: " + request.getUserId());
        logActivity.setIdentity("userId: " + request.getUserId());
        HttpEntity entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);

        throw new UserLoginException("Incorrect password");
    }

    public GetUserResponse getUser(String userId) throws UserNotFoundException {
        ResponseEntity<GetUserResponse> userEntity = restTemplate.getForEntity(userServiceEndpoint + "/user/" + userId, GetUserResponse.class);
        if(userEntity.getStatusCode().is2xxSuccessful())
        {
            return userEntity.getBody();
        }
        throw new UserNotFoundException("User with id " + userId + "not exist!");
    }

    @ExceptionHandler(UserLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserAuthenticationException(UserLoginException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFoundException(UserNotFoundException e) {
        return Map.of("error", e.getMessage());
    }
}
