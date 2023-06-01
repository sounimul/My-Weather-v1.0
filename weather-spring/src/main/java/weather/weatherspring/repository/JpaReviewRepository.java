package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import weather.weatherspring.domain.Review;
import weather.weatherspring.domain.ReviewId;

import java.util.Optional;

public class JpaReviewRepository implements  ReviewRepository{

    private final EntityManager em;

    public JpaReviewRepository(EntityManager em) {
        this.em = em;
    }

    /* 별점, 리뷰 저장 */
    public ReviewId save(Review review){
        ReviewId reviewId = new ReviewId();
        em.persist(review);
        reviewId.setUuid(review.getUuid());
        reviewId.setDate(review.getDate());
        return reviewId;
    }

    /* ReviewId로 값 찾기 */
    public Optional<Review> findById(ReviewId reviewId){
        Review review = em.find(Review.class,reviewId);
        return Optional.ofNullable(review);
    }

    /* 관리자 페이지 - 별점, 리뷰 조회 */


    /* 관리자 페이지 - 별점 평균 조회 */


}
