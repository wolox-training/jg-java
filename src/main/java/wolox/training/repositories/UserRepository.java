package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select u from Users u where ( u.birthdate < ?1 or cast(?1 as date) is null ) and ( u.birthdate > ?2 or cast(?2 as date) is null ) and ( u.name like %?3% or ?3 is null)")
    List<User> findByBirthdateBetweenAndNameIgnoreCaseContaining(LocalDate before, LocalDate after, String name, Pageable pageable);

}
