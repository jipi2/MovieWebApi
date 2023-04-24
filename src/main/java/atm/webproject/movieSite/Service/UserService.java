package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Dtos.ReviewCreateDto;
import atm.webproject.movieSite.Dtos.UserGetDto;
import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Entity.Review;
import atm.webproject.movieSite.Entity.Role;
import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Repository.MovieRepository;
import atm.webproject.movieSite.Repository.ReviewRepository;
import atm.webproject.movieSite.Repository.RoleRepository;
import atm.webproject.movieSite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    private final UserRepository _userRepository;
    private final MovieRepository _movieRepository;
    private final ReviewRepository _reviewRepository;
    private final RoleRepository _roleRepository;

    @Autowired
    public UserService(UserRepository _userRepository, MovieRepository movieRepository, ReviewRepository reviewRepository, RoleRepository roleRepository) {
        this._userRepository = _userRepository;
        this._movieRepository = movieRepository;
        this._reviewRepository = reviewRepository;
        this._roleRepository = roleRepository;
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
        User user = _userRepository.findById(userId).get();
        List<Review> reviews = new ArrayList<>(user.getReviews());

        for(Review r : reviews)
        {
            _reviewRepository.deleteById(r.getId());
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

    public void saveReview(Long userId, Long movieId, ReviewCreateDto reviewDto)
    {
        boolean existsUser = _userRepository.existsById(userId);
        boolean existsMovie = _movieRepository.existsById(movieId);

        if(!existsUser)
        {
            throw new IllegalStateException("user with id "+userId+" does not exist in database");
        }

        if(!existsMovie)
        {
            throw new IllegalStateException("movie with id "+movieId+" does not exist in database");
        }

        List<Review> reviews = _reviewRepository.findAll();
        for (Review rev : reviews)
        {
            if(Objects.equals(rev.getMovie().getId(), movieId) && Objects.equals(rev.getUser().getId(), userId))
            {
                throw new IllegalStateException("User with id: "+userId+" already has a review at this movie");
            }
        }

        Review review = new Review(reviewDto.getContent(), reviewDto.getNumberOfStars());
        review.assignUser(_userRepository.getReferenceById(userId));
        review.assignMovie(_movieRepository.getReferenceById(movieId));

        _reviewRepository.save(review);
    }

    public UserGetDto getUserById(Long userId)
    {
        boolean exists = _userRepository.existsById(userId);
        if(!exists)
        {
            throw new IllegalStateException("user with id "+userId+" does not exist in database");
        }

        User user =  _userRepository.findById(userId).get();
        return new UserGetDto(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getNumberOfPoints());
    }

    public void addRole(Role role)
    {
        _roleRepository.save(role);
    }
}
