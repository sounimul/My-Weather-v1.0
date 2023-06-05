package weather.weatherspring.repository;

import weather.weatherspring.domain.Review;
import weather.weatherspring.domain.ReviewId;

import java.util.Optional;

public interface ReviewRepository {
    ReviewId save(Review review);
    Optional<Review> findById(ReviewId reviewId);
}
