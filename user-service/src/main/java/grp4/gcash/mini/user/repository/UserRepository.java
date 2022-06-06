package grp4.gcash.mini.user.repository;

import grp4.gcash.mini.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsById(String userId);
    List<User> findAllByUserIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String userId, String firstName, String lastName);
}
