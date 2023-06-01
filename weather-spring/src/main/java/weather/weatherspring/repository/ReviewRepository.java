package weather.weatherspring.repository;

import weather.weatherspring.domain.Review;
import weather.weatherspring.domain.ReviewId;

import java.util.Optional;

public interface ReviewRepository {
    public ReviewId save(Review review);
    public Optional<Review> findById(ReviewId reviewId);
}
