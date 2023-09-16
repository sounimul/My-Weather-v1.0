package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Review;
import weather.weatherspring.entity.ReviewId;
import weather.weatherspring.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

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
    /* 모든 리뷰 조회 */
    public Page<Review> findAllReview(int page){
        return reviewRepository.findAll(page);
    }
    /* 평균 별점 조회 */
    public Optional<Double> findAverage(){
        return reviewRepository.calAverage();
    }

    /* 별점 통계 조회 */
    public List<Object[]> findStarCount(){
        return reviewRepository.countStars();
    }

}
