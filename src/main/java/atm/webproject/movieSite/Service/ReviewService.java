package atm.webproject.movieSite.Service;

import atm.webproject.movieSite.Dtos.ReviewMovieDto;
import atm.webproject.movieSite.Dtos.ReviewValidationDto;
import atm.webproject.movieSite.Entity.Review;
import atm.webproject.movieSite.Repository.MovieRepository;
import atm.webproject.movieSite.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import atm.webproject.movieSite.Entity.User;

@Service
public class ReviewService
{
    private final ReviewRepository _reviewRepository;
    private final UserService _userService;
    private final MovieService _movieService;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService, MovieService movieService) {
        _reviewRepository = reviewRepository;
        _userService = userService;
        _movieService = movieService;
    }
    public List<Review> getReviews()
    {
        return _reviewRepository.findAll();
    }

    public void deleteReview(String token, Long reviewId)
    {
        if(!verifyAdmin(token))
            throw new IllegalStateException("You are not admin");

        boolean exits = _reviewRepository.existsById(reviewId);
        if(!exits)
        {
            throw new IllegalStateException("review with id "+reviewId+" does not exist in database");
        }

        Optional<Review> review = _reviewRepository.findById(reviewId);
        review.get().setUser(null);
        review.get().setMovie(null);

        _reviewRepository.deleteById(reviewId);
    }

    public List<ReviewMovieDto> getMovieReviews(Long movieId)
    {
       List<Review> reviews = _reviewRepository.findAll();
       List<ReviewMovieDto> reviewToSend = new ArrayList<>();
       for(Review rew : reviews)
       {
           if(Objects.equals(rew.getMovie().getId(), movieId))
           {
               if (rew.isVerified() == true)
                    reviewToSend.add(new ReviewMovieDto(rew.getContent(), rew.getUser().getUsername(), rew.getUser().getName(), rew.getNumberOfStars()));
           }
       }
       return reviewToSend;
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

    public List<ReviewValidationDto> getUnverifiedReviews(String token)
    {
       if(!verifyAdmin(token))
           throw new IllegalStateException("You are not admin");

        List<Review> reviews =  _reviewRepository.findAll().stream().filter(review -> review.isVerified() == false ).collect(Collectors.toList());
        List<ReviewValidationDto> revList = new ArrayList<>();

        for(Review rew : reviews)
        {
            revList.add(new ReviewValidationDto(rew.getId(), rew.getUser().getUsername(), rew.getContent()));
        }
        return revList;
    }

    public void validateReview(String token, Long reviewId)
    {
        if(!verifyAdmin(token))
            throw new IllegalStateException("You are not admin");

       Optional<Review> revOpt = _reviewRepository.findById(reviewId);

       if(!revOpt.isPresent())
       {
           throw new IllegalStateException("review with id "+reviewId+" does not exist in database");
       }
       Review review = revOpt.get();
       review.setVerified(true);
       _reviewRepository.save(review);

       Long movieId = review.getMovie().getId();
       _movieService.updateRating(movieId);
    }
}
