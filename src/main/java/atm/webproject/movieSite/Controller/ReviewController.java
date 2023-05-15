package atm.webproject.movieSite.Controller;

import atm.webproject.movieSite.Dtos.ReviewMovieDto;
import atm.webproject.movieSite.Dtos.ReviewValidationDto;
import atm.webproject.movieSite.Entity.Review;
import atm.webproject.movieSite.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/review")
public class ReviewController {

    private final ReviewService _reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        _reviewService = reviewService;
    }

    @GetMapping("/getReviews")
    public List<Review> getReviews()
    {
        return _reviewService.getReviews();
    }

    @GetMapping("/getUnverifiedReviews")
    public List<ReviewValidationDto> getUnverifiedReviews( @RequestHeader(name = "Authorization") String authorizationHeader) throws IllegalAccessException {

        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            return _reviewService.getUnverifiedReviews(token);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @PutMapping("/validateReview/{reviewId}")
    public void validateReview(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("reviewId") Long reviewId) throws IllegalAccessException
    {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _reviewService.validateReview(token, reviewId);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }


    @DeleteMapping(path = "/deleteReview/{reviewId}")
    public void deleteReview(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable("reviewId")Long reviewId) throws IllegalAccessException
    {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            _reviewService.deleteReview(token ,reviewId);
        }
        else
            throw new IllegalAccessException("Jwt nu e bun, nuu a intrat in service");
    }

    @GetMapping("/getMovieReview/{movieId}")
    public List<ReviewMovieDto> getMovieReviews(@PathVariable("movieId")Long movieId)
    {
        return _reviewService.getMovieReviews(movieId);
    }
}
