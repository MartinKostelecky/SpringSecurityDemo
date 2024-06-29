package cz.martinkostelecky.springsecurity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<cz.martinkostelecky.springsecurity.user.User,Long> {

    Optional<cz.martinkostelecky.springsecurity.user.User> findByEmail(String email);

}
