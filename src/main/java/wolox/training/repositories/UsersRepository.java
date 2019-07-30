package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Users;

public interface UsersRepository extends CrudRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

}
