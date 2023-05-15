package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Configuration.AuthenticationResponse;
import atm.webproject.movieSite.Configuration.JwtService;
import atm.webproject.movieSite.Dtos.*;
import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Entity.Review;
import atm.webproject.movieSite.Entity.Role;
import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Repository.MovieRepository;
import atm.webproject.movieSite.Repository.ReviewRepository;
import atm.webproject.movieSite.Repository.RoleRepository;
import atm.webproject.movieSite.Repository.UserRepository;
import atm.webproject.movieSite.utils.HashClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class UserService{

    private final UserRepository _userRepository;
    private final MovieRepository _movieRepository;
    private final ReviewRepository _reviewRepository;
    private final RoleRepository _roleRepository;
    private final HashClass _passwordEncoder;
    private final JwtService _jwtService;
    private final AuthenticationManager _authenticationManager;

    @Autowired
    public UserService(UserRepository _userRepository, MovieRepository movieRepository, ReviewRepository reviewRepository, RoleRepository roleRepository, HashClass passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this._userRepository = _userRepository;
        this._movieRepository = movieRepository;
        this._reviewRepository = reviewRepository;
        this._roleRepository = roleRepository;
        this._passwordEncoder = passwordEncoder;
        this._jwtService = jwtService;
        this._authenticationManager = authenticationManager;
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

        Optional<User> userOptional2 = _userRepository.findUserByUsername(user.getUsername());

        if(userOptional2.isPresent())
        {
            throw new IllegalStateException("username taken");
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
        else {
            User user = _userRepository.findById(userId).get();
            List<Review> reviews = new ArrayList<>(user.getReviews());

            for (Review r : reviews) {
                _reviewRepository.deleteById(r.getId());
            }
            _userRepository.deleteById(userId);
        }
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

    public void addMovieToWatchList(String token, Long movieId)
    {
        String username = _jwtService.extractUsername(token);
        Optional<User> userOpt = _userRepository.findUserByUsername(username);
        if(!userOpt.isPresent())
            throw new IllegalStateException("This user does not exists in database");



        User user = userOpt.get();
        Optional<Movie> movieOpt = _movieRepository.findById(movieId);
        if(!movieOpt.isPresent())
            throw new IllegalStateException("This movie does not exist");

        Movie movie = movieOpt.get();
        for(Movie m : user.getSavedMovies())
        {
            if(Objects.equals(movie.getName(), m.getName()))
            {
                throw new IllegalStateException("This movie is already in your watch list");
            }
        }

        user.addMovieToWatchList(movie);
        _userRepository.save(user);
    }
    public void saveReview(String token, Long movieId, ReviewCreateDto reviewDto)
    {
        String username = _jwtService.extractUsername(token);
        Optional<User> userOpt = _userRepository.findUserByUsername(username);
        if(!userOpt.isPresent())
            throw new IllegalStateException("This user does not exists in database");

        User user = userOpt.get();
        Optional<Movie> movieOpt = _movieRepository.findById(movieId);
        if(!movieOpt.isPresent())
            throw new IllegalStateException("This movie does not exist");

        List<Review> reviews = _reviewRepository.findAll();
        for (Review rev : reviews)
        {
            if(Objects.equals(rev.getMovie().getId(), movieId) && Objects.equals(rev.getUser().getId(), user.getId()))
            {
                throw new IllegalStateException("User with id: "+user.getId()+" already has a review at this movie");
            }
        }

        Review review = new Review(reviewDto.getContent(), reviewDto.getNumberOfStars());
        review.assignUser(_userRepository.getReferenceById(user.getId()));
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

    public AuthenticationResponse registerUser(UserRegisterDto userDto) throws NoSuchAlgorithmException {
        Optional<User> userOptional = _userRepository.findUserByEmail(userDto.getEmail());

        if(userOptional.isPresent())
        {
            throw new IllegalStateException("email taken");
        }

        Optional<User> userOptional2 = _userRepository.findUserByUsername(userDto.getUsername());

        if(userOptional2.isPresent())
        {
            throw new IllegalStateException("username taken");
        }

        User user = new User(userDto.getName(), userDto.getUsername(), userDto.getEmail(),_passwordEncoder.encode(userDto.getPassword()), 0);
        Role role = _roleRepository.findRoleByRoleName("client").get();

        user.addRoleToUser(role);
        _userRepository.save(user);

        var jwtToken = _jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse loginUser(UserLoginDto userDto) throws NoSuchAlgorithmException {
//        _authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userDto.getUsername(),
//                        userDto.getPassword()
//                )
//        );



        Optional<User> userOptional = _userRepository.findUserByUsername(userDto.getUsername());

        if(userOptional.isEmpty())
        {
            throw new IllegalStateException("Bad credentials");
        }

        User user = _userRepository.findUserByUsername(userDto.getUsername()).get();

        if(!Objects.equals(user.getPassword(), _passwordEncoder.encode(userDto.getPassword())))
            throw new IllegalStateException("Bad credentials");


        var jwtToken = _jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public Optional<User> getUsernameFromJwt(String token)
    {
        String username = _jwtService.extractUsername(token);
        Optional<User> userOptional = _userRepository.findUserByUsername(username);
        if(!userOptional.isPresent())
        {
            throw new IllegalStateException("Bad jwt, user does not exist");
        }
        return userOptional;
    }

    public UserGetDto getUserDtoFromJwt(String token)
    {
       Optional<User> userOptional = getUsernameFromJwt(token);

        UserGetDto userDto = new UserGetDto(userOptional.get().getId(), userOptional.get().getName(), userOptional.get().getUsername(), userOptional.get().getEmail(), userOptional.get().getNumberOfPoints());
        return userDto;
    }

    public void updatePass(String token, PassUpdate passDto)  {
        Optional<User> userOpt = getUsernameFromJwt(token);
        String oldPass = userOpt.get().getPassword();
        String newPass =  _passwordEncoder.encode(passDto.getNewPass());
        String oldPassGot = _passwordEncoder.encode(passDto.getOldPass());

        if(!Objects.equals(oldPass, oldPassGot))
            throw  new IllegalStateException("The old password is not oke");

        User user = userOpt.get();
        user.setPassword(newPass);
        _userRepository.save(user);
    }

    public void updateEmail(String token, EmailUpdate emailDto)
    {
        Optional<User> userOpt = getUsernameFromJwt(token);

        String oldEmail = userOpt.get().getEmail();
        String newEmail =  emailDto.getNewEmail();
        String oldEmailGot = emailDto.getOldEmail();

        if(!Objects.equals(oldEmailGot, oldEmail))
            throw  new IllegalStateException("The old email is not oke");

        User user = userOpt.get();
        user.setEmail(newEmail);
        _userRepository.save(user);
    }

    public void updateName(String token, NameUpdate nameDto)
    {
        Optional<User> userOpt = getUsernameFromJwt(token);

        String oldName = userOpt.get().getName();
        String newName =  nameDto.getNewName();
        String oldNameGot = nameDto.getOldName();

        if(!Objects.equals(oldNameGot, oldName))
            throw  new IllegalStateException("The old name is not oke");

        User user = userOpt.get();
        user.setName(newName);
        _userRepository.save(user);
    }

    public AuthenticationResponse updateUsername(String token, UsernameUpdate usernameDto)
    {
        Optional<User> userOpt = getUsernameFromJwt(token);

        String oldUsername = userOpt.get().getUsername();
        String newUsername =  usernameDto.getNewUsername();
        String oldUsernameGot = usernameDto.getOldUsername();

        if(!Objects.equals(oldUsername, oldUsernameGot))
            throw  new IllegalStateException("The old username is not oke");

        Optional<User> userOptTest = _userRepository.findUserByUsername(newUsername);
        if(userOptTest.isPresent())
            throw new IllegalStateException("This username is already taken");

        User user = userOpt.get();
        user.setUsername(newUsername);
        _userRepository.save(user);

        var jwtToken = _jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public List<Movie> getWatchList(String token)
    {
        String username = _jwtService.extractUsername(token);
        Optional<User> userOpt = _userRepository.findUserByUsername(username);
        if(!userOpt.isPresent())
        {
            throw new IllegalStateException("user with username: "+username+" does not exist in database");
        }
        User user = userOpt.get();
        return new ArrayList<>(user.getSavedMovies());
    }

    public List<UserGetDto> getNormalUsers(String token)
    {
        Optional<User> useropt = getUsernameFromJwt(token);
        if(!useropt.isPresent())
            throw new IllegalStateException("You are not admin");

        if(!Objects.equals(useropt.get().getSavedRoles().stream().findFirst().get().getRoleName(), "admin"))
        {
            throw new IllegalStateException("You are not admin");
        }

        List <User> userList = _userRepository.findAll();
        List<UserGetDto> normalUsersList = new ArrayList<>();

        for(User u:userList)
        {
            if(Objects.equals(u.getSavedRoles().stream().findFirst().get().getRoleName(),"client"))
            {
                normalUsersList.add(new UserGetDto(u.getId(), u.getName(), u.getUsername(), u.getEmail(), u.getNumberOfPoints()));
            }
        }

        return normalUsersList;
    }
}
