package weather.weatherspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.entity.Review;
import weather.weatherspring.domain.entity.ReviewId;
import weather.weatherspring.domain.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    /* 리뷰 저장하기 */
    @Transactional
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
