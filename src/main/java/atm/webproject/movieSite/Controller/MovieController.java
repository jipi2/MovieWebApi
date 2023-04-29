package atm.webproject.movieSite.Controller;

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


    @GetMapping("/getGenderMovies/{gender}")
//    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    public List<Movie> getGenderMovies(@PathVariable("gender") String gender) {

        return _movieService.getGenderMovies(gender);
    }

    @PostMapping("/addMovie")
    public void addMovie(@RequestBody Movie movie)
    {
        _movieService.addNewMovie(movie);
    }

    @DeleteMapping(path = "/deleteMovie/{movieId}")
    public void deleteMovie(@PathVariable("movieId")Long movieId)
    {
        _movieService.deleteMovie(movieId);
    }
}
