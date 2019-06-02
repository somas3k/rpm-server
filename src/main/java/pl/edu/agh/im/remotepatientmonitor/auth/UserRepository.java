package pl.edu.agh.im.remotepatientmonitor.auth;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<ApplicationUser, Long> {
    ApplicationUser findByEmail(String email);

    @Query("from ApplicationUser as user where user.enabled = 'True'")
    List<ApplicationUser> searchExpired();
}



