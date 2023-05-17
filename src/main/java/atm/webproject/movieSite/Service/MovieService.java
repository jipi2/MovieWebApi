package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Dtos.MovieInfoDto;
import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Entity.Review;
import atm.webproject.movieSite.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import atm.webproject.movieSite.Entity.User;
@Service
public class MovieService {
    private final MovieRepository _movieRepository;
    private final UserService _userService;

    @Autowired
    public MovieService(MovieRepository _movieRepository, UserService userService) {
        this._movieRepository = _movieRepository;
        this._userService = userService;
    }

    private boolean verifyAdmin(String token)
    {
        Optional<User> userOpt = _userService.getUsernameFromJwt(token);
        if(!userOpt.isPresent())
        {
            return false;
        }

        if(!Objects.equals(userOpt.get().getSavedRoles().stream().findFirst().get().getRoleName(), "admin"))
        {
            return false;
        }
        return true;
    }
    public List<Movie> getMovies()
    {
        return _movieRepository.findAll();
    }

    public List<Movie> getGenderMovies(String gender)
    {
        return _movieRepository.findMovieByGender(gender);
    }

    public void addNewMovie(String token, Movie movie)
    {
        if (!verifyAdmin(token))
            throw new IllegalStateException("You are not admin");

        Optional<Movie> movieOptional = _movieRepository.findMovieByName(movie.getName());
        if(movieOptional.isPresent())
        {
            throw new IllegalStateException("this movie is already in database");
        }
        _movieRepository.save(movie);
    }

    public void deleteMovie(Long movieId)
    {
        boolean exists = _movieRepository.existsById(movieId);
        if(!exists)
        {
            throw new IllegalStateException("movie with id "+ movieId + " does not exists");
        }
        else
            _movieRepository.deleteById(movieId);
    }

    public MovieInfoDto getMovie(Long movieId)
    {
        Optional<Movie> movieOpt = _movieRepository.findById(movieId);
        if(!movieOpt.isPresent())
            throw new IllegalStateException("This movie does not exist");

        MovieInfoDto movieDto = new MovieInfoDto(
                movieOpt.get().getName(),
                movieOpt.get().getDescription(),
                movieOpt.get().getTrailer(),
                movieOpt.get().getPathImage(),
                movieOpt.get().getRating()
        );
        return movieDto;
    }

    public Long getMovieIdByName(String name)
    {
        List<Movie> movieList = _movieRepository.findAll();
        for(Movie m:movieList)
        {
            if (m.getName().toLowerCase().contains(name.toLowerCase()))
            {
                return m.getId();
            }
        }
        return (long) -1;
    }

    public void updateRating(Long movieId)
    {
        Optional<Movie> movieOpt = _movieRepository.findById(movieId);
        if(!movieOpt.isPresent())
        {
            throw new IllegalStateException("This movie does not exist");
        }

        double rating = 0;
        int count = 0;

        for(Review review:movieOpt.get().getReviews())
        {
            rating+=(review.getNumberOfStars()*2);
            count++;
        }
        rating = rating/count;

        movieOpt.get().setRating(rating);
        _movieRepository.save(movieOpt.get());
    }
}
