package atm.webproject.movieSite.Controller;

import atm.webproject.movieSite.Configuration.AuthenticationResponse;
import atm.webproject.movieSite.Dtos.*;
import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Entity.Role;
import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController
{
    private final UserService _userService;

    @Autowired
    public UserController(UserService _userService) {
        this._userService = _userService;
    }

    @GetMapping("/getUsers")
    public List<User> getUsers(
    )
    {
        return _userService.getUsers();
    }

    @GetMapping("/getNormalUsers")
    public List<UserGetDto> getNormalUsers( @RequestHeader(name = "Authorization") String authorizationHeader) throws IllegalAccessException
    {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return _userService.getNormalUsers(token);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @GetMapping("/UsersExceptThisUser")
    public List<UserGetDto> getUsersExceptThisOne( @RequestHeader(name = "Authorization") String authorizationHeader) throws IllegalAccessException
    {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return _userService.getUsersExceptThisOne(token);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }
    @PostMapping("/addUser")
    public void addUser(@RequestBody User user)
    {
        _userService.addNewUser(user);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserRegisterDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(_userService.registerUser(userDto));
    }
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserLoginDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(_userService.loginUser(userDto));
    }

    @DeleteMapping(path = "/deleteUser/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId)
    {
        _userService.deleteUser(userId);
    }

    @PutMapping(path = "/updateUser/{userId}")
    public void updateUser(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email)
    {
        _userService.updateUser(userId, name, email);
    }

    @PutMapping(path="/updatePassword")
    public void updatePass(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody PassUpdate passDto
            ) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.updatePass(token, passDto);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping(path = "/makeAdmin/{userId}")
    public void makeAdmin(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.makeAdmin(token, userId);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping(path = "/deleteAdminRights/{userId}")
    public void deleteAdminRights(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.deleteAdminPriv(token, userId);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @GetMapping(path = "/isAdmin")
    public boolean verifyIsAdmin(
            @RequestHeader(name = "Authorization") String authorizationHeader) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return _userService.verifyIsAdmin(token);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping(path="/updateEmail")
    public void updateEmail(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody EmailUpdate emailDto
    ) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.updateEmail(token, emailDto);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping(path="/updateName")
    public void updateName(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody NameUpdate nameDto
    ) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.updateName(token, nameDto);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping(path="/updateUsername")
    public AuthenticationResponse updateUsername(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody UsernameUpdate usernameDto
    ) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return _userService.updateUsername(token, usernameDto);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping("/addMovieWatchList/movie/{movieId}")
    public void saveMovieToWatchList(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("movieId") Long movieId
    ) throws IllegalAccessException
    {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.addMovieToWatchList(token, movieId);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");

    }

    @PutMapping("/removeMovieWatchList/movie/{movieId}")
    public void removeMovieFromWatchList(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("movieId") Long movieId
    ) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.removeMovieFromWatchList(token, movieId);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }


    @GetMapping("/getWatchList")
    public List<Movie> getWatchList(
            @RequestHeader(name = "Authorization") String authorizationHeader) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return  _userService.getWatchList(token);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");

    }

    @PostMapping("/saveReview/movie/{movieId}")
    public ResponseEntity<Object> saveReview(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("movieId")Long movieId,
            @RequestBody ReviewCreateDto reviewDto
            ) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _userService.saveReview(token, movieId, reviewDto);
            return ResponseEntity.ok().build();
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @GetMapping("getUserById/{userId}")
    public UserGetDto getUserById(@PathVariable("userId")Long userId)
    {
        return _userService.getUserById(userId);
    }

    @GetMapping("getUserFromJwt")
    public UserGetDto getUserFromJwt(@RequestHeader(name = "Authorization") String authorizationHeader) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return _userService.getUserDtoFromJwt(token);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a instrat in service");

    }



    @PostMapping("/addRole")
    public void getUserById(@RequestBody Role role)
    {
         _userService.addRole(role);
    }
}
