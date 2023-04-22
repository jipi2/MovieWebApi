package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Entity.Movie;
import atm.webproject.movieSite.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository _movieRepository;

    @Autowired
    public MovieService(MovieRepository _movieRepository) {
        this._movieRepository = _movieRepository;
    }

    public List<Movie> getMovies()
    {
        return _movieRepository.findAll();
    }

    public List<Movie> getGenderMovies(String gender)
    {
        return _movieRepository.findMovieByGender(gender);
    }

    public void addNewMovie(Movie movie)
    {
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
}
