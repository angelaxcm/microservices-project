package grp4.gcash.mini.apiservice.controller;

import con.tbs.payload.GetAllActivitiesResponse;
import grp4.gcash.mini.apiservice.exceptions.GetAllException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("activity")
public class ActivityController {
    private final RestTemplate restTemplate;
    private final String activityServiceEndpoint;

    public ActivityController(RestTemplate restTemplate, @Value("${activity-service.endpoint}") String activityServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.activityServiceEndpoint = activityServiceEndpoint;
    }

    @GetMapping("{id}")
    public GetAllActivitiesResponse getMyActivities(@PathVariable(name = "id")  String userId) throws GetAllException {
        //TODO
        ResponseEntity<GetAllActivitiesResponse> response = restTemplate.getForEntity(activityServiceEndpoint + "/activity/search?q=" + userId , GetAllActivitiesResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new GetAllException("Get activities failed");
    }

    @ExceptionHandler(GetAllException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleGetAllException(GetAllException e) {
        return Map.of("error", e.getMessage());
    }
}
