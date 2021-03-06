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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
    @PostMapping("register")
    public UserRegistrationResponse registerUser(@Valid @RequestBody UserRegistrationRequest request) throws UserRegistrationException {

        if (userRepository.existsById(request.getUserId())) {
            logActivity.setAction(""+LogActivityActions.USER_REGISTRATION_FAILED);
            logActivity.setInformation("userId: " + request.getUserId() + " email: " + request.getEmail() + " firstName: " + request.getFirstName() + " lastName:" + request.getLastName());
            logActivity.setIdentity("userId: " + request.getUserId());
            entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
            throw new UserRegistrationException("User already registered!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            logActivity.setAction(""+LogActivityActions.USER_REGISTRATION_FAILED);
            logActivity.setInformation("userId: " + request.getUserId() + " email: " + request.getEmail() + " firstName: " + request.getFirstName() + " lastName:" + request.getLastName());
            logActivity.setIdentity("userId: " + request.getUserId());
            entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
            throw new UserRegistrationException("Email not available!");
        }

        //Create Wallet
        CreateWallet createWallet = new CreateWallet(request.getUserId());
        ResponseEntity<Void> createWalletEntity = restTemplate.postForEntity(walletServiceEndpoint + "/wallet",createWallet, Void.class);
        if (!createWalletEntity.getStatusCode().is2xxSuccessful()) {
           throw new UserRegistrationException("HTTP " + createWalletEntity.getStatusCodeValue());
        }
        User user = new User(request.getUserId(), request.getFirstName(),
                request.getLastName(),
                request.getMiddleName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        logActivity.setAction(""+LogActivityActions.USER_REGISTRATION_SUCCESSFUL);
        logActivity.setInformation("userId: " + request.getUserId() + " email: " + request.getEmail() + " firstName: " + request.getFirstName() + " lastName:" + request.getLastName());
        logActivity.setIdentity("userId: " + request.getUserId());
        entity = restTemplate.postForEntity(activityServiceEndpoint + "/activity", logActivity, LogActivity.class);
        return new UserRegistrationResponse(savedUser.getUserId());
    }

    @GetMapping("{id}")
    public GetUserResponse getUser(@PathVariable String id) throws UserNotFoundException {
        User user = getThisUser(id);
        Double balance = null;

        ResponseEntity<GetWalletResponse> senderEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + user.getUserId(), GetWalletResponse.class);
        if (senderEntity.getStatusCode().is2xxSuccessful()) {
            GetWalletResponse receiver = senderEntity.getBody();
            balance = receiver.getBalance();
        }

        GetUserResponse response = new GetUserResponse(user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPassword(),balance);

        response.setLastLoggedIn(user.getLastLoggedIn());
        response.setLastUpdated(user.getLastLoggedIn());
        response.setDateCreated(user.getDateCreated());

        logActivity.setAction(""+LogActivityActions.GET_SUCCESS);
        logActivity.setInformation("userId: " + id);
        logActivity.setIdentity("userId: " + id);
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

    @GetMapping("search")
    public GetAllUsersResponse search(@RequestParam(name = "q", required = false, defaultValue = "") String query) {
        List<UserDetails> users = new ArrayList<>();

        Consumer<User> userConsumer = user -> {
            Double balance = null;
            ResponseEntity<GetWalletResponse> senderEntity = restTemplate.getForEntity(walletServiceEndpoint + "/wallet/" + user.getUserId(), GetWalletResponse.class);
            if (senderEntity.getStatusCode().is2xxSuccessful()) {
                GetWalletResponse receiver = senderEntity.getBody();
                balance = receiver.getBalance();
            }

            users.add(new UserDetails(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), balance, user.getDateCreated()));
        };
        if (query.isBlank()) {
            userRepository.findAll()
                    .forEach(userConsumer);
        } else {
            userRepository.findAllByUserIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query, query)
                    .forEach(userConsumer);
        }

        GetAllUsersResponse response = new GetAllUsersResponse((long) users.size(), users);
        return response;
    }

    public void createWallet(String userId){

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
