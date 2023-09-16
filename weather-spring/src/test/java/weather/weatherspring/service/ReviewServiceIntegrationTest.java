package weather.weatherspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Review;
import weather.weatherspring.entity.ReviewId;
import weather.weatherspring.repository.ReviewRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReviewServiceIntegrationTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;


    @Test
    public void 별점리뷰_저장(){
        // Given
        Review review = new Review();

        review.setUuid(1L);
        review.setDate(LocalDateTime.now());
        review.setStars(4);
        review.setComments("만족합니다.");

        // When
        ReviewId savedReviewId=reviewService.saveReview(review);

        // Then
        Review findReview = reviewRepository.findById(savedReviewId).get();
        assertThat(review).isSameAs(findReview);

    }

}