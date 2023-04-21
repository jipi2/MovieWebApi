package atm.webproject.movieSite.Controller;

import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController
{
    private  final UserService _userService;

    @Autowired
    public UserController(UserService _userService) {
        this._userService = _userService;
    }

    @GetMapping("/getUsers")
    public List<User> getUsers()
    {
        return _userService.getUsers();
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user)
    {
        _userService.addNewUser(user);
    }

    @DeleteMapping(path = "/deleteUser/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
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


}
