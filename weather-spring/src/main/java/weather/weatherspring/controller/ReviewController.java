package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import weather.weatherspring.domain.entity.Review;
import weather.weatherspring.domain.dto.ReviewForm;
import weather.weatherspring.service.ReviewService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private HttpServletRequest request;

    private final ReviewService reviewService;

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
