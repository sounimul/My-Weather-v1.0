package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import weather.weatherspring.entity.Review;
import weather.weatherspring.domain.ReviewForm;
import weather.weatherspring.service.ReviewService;

import java.time.LocalDateTime;

@Controller
public class ReviewController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public String submitReview(ReviewForm reviewForm){
        HttpSession session = request.getSession();
        Review review = new Review();

        if(reviewForm.getReviewStar()==0) reviewForm.setReviewStar(-1);

        review.setUuid((Long) session.getAttribute("uid"));
        review.setDate(LocalDateTime.now());
        review.setStars(reviewForm.getReviewStar());
        review.setComments(reviewForm.getComment());

        reviewService.saveReview(review);

        return "redirect:/weather";
    }

}
