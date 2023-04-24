package atm.webproject.movieSite.Repository;

import atm.webproject.movieSite.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findRoleByRoleName(String roleName);
}
