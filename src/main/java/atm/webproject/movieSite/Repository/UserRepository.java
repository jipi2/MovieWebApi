package atm.webproject.movieSite.Repository;

import atm.webproject.movieSite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findUserByEmail (String email);
    Optional<User> findUserByUsername (String username);
}
