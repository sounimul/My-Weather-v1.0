package weather.weatherspring.domain.repository;

import org.springframework.data.domain.Page;
import weather.weatherspring.domain.entity.Review;
import weather.weatherspring.domain.entity.ReviewId;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    ReviewId save(Review review);
    Optional<Review> findById(ReviewId reviewId);
    Page<Review> findAll(int page);
    Optional<Double> calAverage();
    List<Object[]> countStars();
}
