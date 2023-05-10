package atm.webproject.movieSite.Controller;

import atm.webproject.movieSite.Dtos.ReviewMovieDto;
import atm.webproject.movieSite.Dtos.ReviewValidationDto;
import atm.webproject.movieSite.Entity.Review;
import atm.webproject.movieSite.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ReviewValidationDto> getUnverifiedReviews()
    {
        return _reviewService.getUnverifiedReviews();
    }

    @PutMapping("/validateReview/{reviewId}")
    public void validateReview(@PathVariable("reviewId") Long reviewId)
    {
        _reviewService.validateReview(reviewId);
    }


    @DeleteMapping(path = "/deleteReview/{reviewId}")
    public void deleteReview(@PathVariable("reviewId")Long reviewId)
    {
        _reviewService.deleteReview(reviewId);
    }

    @GetMapping("/getMovieReview/{movieId}")
    public List<ReviewMovieDto> getMovieReviews(@PathVariable("movieId")Long movieId)
    {
        return _reviewService.getMovieReviews(movieId);
    }
}
