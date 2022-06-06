package grp4.gcash.mini.user.controller;

import con.tbs.payload.*;
import grp4.gcash.mini.user.model.User;
import grp4.gcash.mini.user.exception.*;
import grp4.gcash.mini.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    private final String activityServiceEndpoint;
    private final String walletServiceEndpoint;

    private LogActivity logActivity = new LogActivity();
    private HttpEntity<LogActivity> entity;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RestTemplate restTemplate,
                          @Value("${activity-service.endpoint}") String activityServiceEndpoint,
                          @Value("${wallet-service.endpoint}") String walletServiceEndpoint) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.activityServiceEndpoint = activityServiceEndpoint;
        this.walletServiceEndpoint = walletServiceEndpoint;
    }

    @PostMapping
    public UserRegistrationResponse register(@Valid @RequestBody UserRegistrationRequest request) throws UserRegistrationException {

        if (userRepository.existsById(request.getUserId())) {
            logActivity.setAction(""+LogActivityActions.USER_REGISTRATION_FAILED);
            logActivity.setInformation("mobileNumber: " + request.getUserId() + " email: " + request.getEmail() + " firstName: " + request.getFirstName() + " lastName:" + request.getLastName());
            logActivity.setIdentity("mobileNumber: " + request.getUserId());
            entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
            throw new UserRegistrationException("User already registered!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            logActivity.setAction(""+LogActivityActions.USER_REGISTRATION_FAILED);
            logActivity.setInformation("mobileNumber: " + request.getUserId() + " email: " + request.getEmail() + " firstName: " + request.getFirstName() + " lastName:" + request.getLastName());
            logActivity.setIdentity("mobileNumber: " + request.getUserId());
            entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
            throw new UserRegistrationException("Email not available!");
        }

        User user = new User(request.getUserId(), request.getFirstName(),
                request.getLastName(),
                request.getMiddleName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        logActivity.setAction(""+LogActivityActions.USER_REGISTRATION_SUCCESSFUL);
        logActivity.setInformation("mobileNumber: " + request.getUserId() + " email: " + request.getEmail() + " firstName: " + request.getFirstName() + " lastName:" + request.getLastName());
        logActivity.setIdentity("mobileNumber: " + request.getUserId());
        entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
        return new UserRegistrationResponse(savedUser.getUserId());
    }

    @GetMapping("{id}")
    public GetUserResponse getUser(@PathVariable String id) throws UserNotFoundException {
        User user = getThisUser(id);
        GetUserResponse response = new GetUserResponse(user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPassword(),
                getBalance(user.getUserId()));

        logActivity.setAction(""+LogActivityActions.GET_SUCCESS);
        logActivity.setInformation("mobileNumber: " + id);
        logActivity.setIdentity("mobileNumber: " + id);
        entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
        return response;
    }

    @PostMapping("login/{id}")
    public void userLogin(@PathVariable String id) throws UserNotFoundException {
        User user = getThisUser(id);
        user.setLastLoggedIn(LocalDateTime.now());
        userRepository.save(user);
    }


    @GetMapping("all")
    public GetAllUsersResponse getAllUsers() {
        GetAllUsersResponse response = new GetAllUsersResponse(userRepository.count(), new ArrayList<>());
        userRepository.findAll().forEach(user -> response.getUsers().add(new UserDetails(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), getBalance(user.getUserId()), user.getDateCreated())));

        return response;
    }

    public Double getBalance(String userId) {
        ResponseEntity<GetWalletResponse> entity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + userId, GetWalletResponse.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            GetWalletResponse receiver = entity.getBody();
            return receiver.getBalance();
        }
        return null;
    }

    public User getThisUser(String userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found");
        }
        return user.get();
    }

    @ExceptionHandler(UserRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserRegistrationException(UserRegistrationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFoundException(UserNotFoundException e) {
        return Map.of("error", e.getMessage());
    }
}
