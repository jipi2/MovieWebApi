package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Repository.MovieRepository;
import atm.webproject.movieSite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    private final UserRepository _userRepository;
    private final MovieRepository _movieRepository;

    @Autowired
    public UserService(UserRepository _userRepository, MovieRepository movieRepository) {
        this._userRepository = _userRepository;
        this._movieRepository = movieRepository;
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

    public void addMovieToWatchList(Long userId, Long movieId)
    {
        User user = _userRepository.findById(userId).get();
        Movie movie = _movieRepository.findById(movieId).get();

        user.addMovieToWatchList(movie);
        _userRepository.save(user);
    }

    public List<Movie> getWatchList(long userId)
    {
        boolean exists = _userRepository.existsById(userId);
        if(!exists)
        {
            throw new IllegalStateException("user with id "+userId+" does not exist in database");
        }
        User user = _userRepository.getReferenceById(userId);
        return new ArrayList<>(user.getSavedMovies());
    }
}
