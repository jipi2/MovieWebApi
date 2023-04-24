package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Entity.Role;
import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository _roleRepository;


    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this._roleRepository = roleRepository;
    }

    public void addNewRole(Role role)
    {
        Optional<Role> roleOptional = _roleRepository.findRoleByRoleName(role.getRoleName());

        if(roleOptional.isPresent())
        {
            throw new IllegalStateException("role taken");
        }

        _roleRepository.save(role);
    }
}
