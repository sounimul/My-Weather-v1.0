package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Review;
import weather.weatherspring.domain.ReviewId;
import weather.weatherspring.repository.ReviewRepository;

@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /* 리뷰 저장하기 */
    public ReviewId saveReview(Review review){
        return reviewRepository.save(review);
    }

    /* 관리자 페이지 기능 */


}
