package grp4.gcash.mini.adminservice.controller;

import grp4.gcash.mini.adminservice.exceptions.GetAllException;
import grp4.gcash.mini.adminservice.exceptions.InconsistentResultException;
import grp4.gcash.mini.adminservice.payload.GetAllActivitiesResponse;
import grp4.gcash.mini.adminservice.payload.GetAllUsersResponse;
import grp4.gcash.mini.adminservice.payload.LogActivity;
import grp4.gcash.mini.adminservice.payload.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final RestTemplate restTemplate;
    private final String userServiceEndpoint;
    private final String activityServiceEndpoint;

    public AdminController(RestTemplate restTemplate,
                           @Value("${user-service.endpoint}") String userServiceEndpoint,
                           @Value("${activity-service.endpoint}") String activityServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.userServiceEndpoint = userServiceEndpoint;
        this.activityServiceEndpoint = activityServiceEndpoint;
    }

    @GetMapping("all-users")
    public List<UserDetails> retrieveAllUsers() throws InconsistentResultException, GetAllException {
        ResponseEntity<GetAllUsersResponse> entity = restTemplate.getForEntity(userServiceEndpoint + "/user/all", GetAllUsersResponse.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            GetAllUsersResponse body = entity.getBody();

            if (body.getCurrentUsers() != (long) body.getUsers().size()) {
                throw new InconsistentResultException("Returned number of user didn't match the actual list length");
            }

            return body.getUsers();
        }

        throw new GetAllException("Something went wrong");
    }

    @GetMapping("search-users")
    public List<UserDetails> retrieveUsers(@RequestParam(name = "q", required = false, defaultValue = "") String query) throws InconsistentResultException, GetAllException {
        ResponseEntity<GetAllUsersResponse> entity = restTemplate.getForEntity(userServiceEndpoint + "/user/search?q=" + query, GetAllUsersResponse.class);

        if(entity.getStatusCode().is2xxSuccessful()) {
            GetAllUsersResponse body = entity.getBody();

            if (body.getCurrentUsers() != (long) body.getUsers().size()) {
                throw new InconsistentResultException("Returned number of activities didn't match the actual list length");
            }

            return body.getUsers();
        }

        throw new GetAllException("Something went wrong");
    }

    @GetMapping("all-activities")
    public List<LogActivity> retrieveAllActivities() throws InconsistentResultException, GetAllException {
        ResponseEntity<GetAllActivitiesResponse> entity = restTemplate.getForEntity(activityServiceEndpoint + "/activity/all", GetAllActivitiesResponse.class);

        if(entity.getStatusCode().is2xxSuccessful()) {
            GetAllActivitiesResponse body = entity.getBody();

            if (body.getCurrentActivitiesCount() != (long) body.getActivities().size()) {
                throw new InconsistentResultException("Returned number of activities didn't match the actual list length");
            }

            return body.getActivities();
        }

        throw new GetAllException("Something went wrong");
    }

    @GetMapping("search-activities")
    public List<LogActivity> retrieveActivities(@RequestParam(name = "q", required = false, defaultValue = "") String query) throws InconsistentResultException, GetAllException {
        ResponseEntity<GetAllActivitiesResponse> entity = restTemplate.getForEntity(activityServiceEndpoint + "/activity/search?q=" + query, GetAllActivitiesResponse.class);

        if(entity.getStatusCode().is2xxSuccessful()) {
            GetAllActivitiesResponse body = entity.getBody();

            if (body.getCurrentActivitiesCount() != (long) body.getActivities().size()) {
                throw new InconsistentResultException("Returned number of activities didn't match the actual list length");
            }

            return body.getActivities();
        }

        throw new GetAllException("Something went wrong");
    }

    @ExceptionHandler(InconsistentResultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInconsistentResultException(InconsistentResultException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(GetAllException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleGetAllException(GetAllException e) {
        return Map.of("error", e.getMessage());
    }
}
