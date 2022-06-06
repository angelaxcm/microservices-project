package grp4.gcash.mini.activityservice.repository;

import grp4.gcash.mini.activityservice.model.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
    List<Activity> findAllByActionContainingIgnoreCaseOrInformationContainingIgnoreCaseOrIdentityContainingIgnoreCase(String action, String information, String identity);
}
