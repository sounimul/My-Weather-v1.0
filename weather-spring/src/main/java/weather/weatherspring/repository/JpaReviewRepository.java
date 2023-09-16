package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import weather.weatherspring.entity.Review;
import weather.weatherspring.entity.ReviewId;

import java.util.List;
import java.util.Optional;

public class JpaReviewRepository implements ReviewRepository{

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
    public Page<Review> findAll(int page){
        List<Review> reviews = em.createQuery("select r from Review r order by r.date desc",Review.class)
                .getResultList();
        PageRequest pageRequest = PageRequest.of(page,13);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()), reviews.size());
        Page<Review> reviewPage = new PageImpl<>(reviews.subList(start,end),pageRequest,reviews.size());
        return reviewPage;
    }

    /* 관리자 페이지 - 별점 평균 조회 */
    public Optional<Double> calAverage(){
        Optional<Double> avgStar = Optional.ofNullable(em.createQuery("select avg(r.stars) from Review r where r.stars > 0", Double.class)
                .getSingleResult());
        return avgStar;
    }

    /* 관리자 페이지 - 별점별 개수 조회 */
    public List<Object[]> countStars(){
        List<Object[]> reviews = em.createQuery("select r.stars, count(r.stars) from Review r group by r.stars order by r.stars desc",Object[].class)
                .getResultList();
        return reviews;
    }
}
