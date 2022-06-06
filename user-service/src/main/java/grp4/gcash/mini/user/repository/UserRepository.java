package grp4.gcash.mini.user.repository;

import grp4.gcash.mini.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsById(String userId);
}
