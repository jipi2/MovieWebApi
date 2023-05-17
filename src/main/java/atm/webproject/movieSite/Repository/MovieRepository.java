package atm.webproject.movieSite.Repository;

import atm.webproject.movieSite.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>
{
    List<Movie> findMovieByGender(String gender);
    Optional<Movie> findMovieByName(String name);

}
