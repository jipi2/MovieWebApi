package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository _userRepository;

    @Autowired
    public UserService(UserRepository _userRepository) {
        this._userRepository = _userRepository;
    }

    public List<User> getUsers()
    {
        return _userRepository.findAll();
    }

    public void addNewUser(User user)
    {
        Optional<User> userOptional = _userRepository.findUserByEmail(user.getEmail());

        if(userOptional.isPresent())
        {
            throw new IllegalStateException("email taken");
        }

        _userRepository.save(user);
    }

    public void deleteUser(Long userId)
    {
        boolean exists = _userRepository.existsById(userId);
        if(!exists)
        {
            throw new IllegalStateException("user with id "+ userId + " does not exists");
        }
        _userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String name, String email)
    {
        User user = _userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException(
                        "user with id " + userId+ " does not exist"
                ));
        if(name != null && name.length() > 0 && !Objects.equals(user.getName(), name))
        {
            user.setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email))
        {
            Optional<User> userOptional = _userRepository.findUserByEmail(email);
            if(userOptional.isPresent())
            {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }

    }
}
