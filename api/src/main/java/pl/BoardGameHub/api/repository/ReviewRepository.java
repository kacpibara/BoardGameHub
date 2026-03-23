package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}