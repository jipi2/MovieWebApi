package atm.webproject.movieSite.Controller;

import atm.webproject.movieSite.Dtos.MovieInfoDto;
import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080/JipiMovies/html/home.html")
@RequestMapping(path = "api/movie")
public class MovieController {

    private final MovieService _movieService;

    @Autowired
    public MovieController(MovieService _movieService) {
        this._movieService = _movieService;
    }

    @GetMapping("/getMovies")
    @CrossOrigin(origins = "*")
    public List<Movie> getMovies()
    {
        return _movieService.getMovies();
    }

    @GetMapping("/getMovie/{movieId}")
    public MovieInfoDto getMovie(
            @PathVariable(name = "movieId") Long movieId
    )
    {
        return _movieService.getMovie(movieId);
    }

    @GetMapping("/getMovieIdByName/{name}")
    public Long getIdMovieByName(
            @PathVariable(name = "name") String name
    )
    {
       return _movieService.getMovieIdByName(name);
    }

    @GetMapping("/getGenderMovies/{gender}")
//    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    public List<Movie> getGenderMovies(@PathVariable("gender") String gender) {

        return _movieService.getGenderMovies(gender);
    }

    @PostMapping("/addMovie")
    public void addMovie(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody Movie movie) throws IllegalAccessException {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _movieService.addNewMovie(token,movie);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PostMapping

    @DeleteMapping(path = "/deleteMovie/{movieId}")
    public void deleteMovie(@PathVariable("movieId")Long movieId)
    {
        _movieService.deleteMovie(movieId);
    }
}
