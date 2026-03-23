package pl.BoardGameHub.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.BoardGameHub.api.dto.ReviewRequest;
import pl.BoardGameHub.api.dto.ReviewResponse;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.model.Review;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.ReviewRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BoardGameRepository boardGameRepository;

    public ReviewResponse addReview(ReviewRequest request) {
        // 1. Walidacja gwiazdek
        if (request.stars() < 1 || request.stars() > 5) {
            throw new IllegalStateException("Ocena musi wynosić od 1 do 5 gwiazdek!");
        }

        // 2. Szukamy gry w bazie
        BoardGame game = boardGameRepository.findById(request.gameId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono gry o podanym ID!"));

        // 3. Budujemy encję (zauważ, że datę ustawiamy sztywno na dzisiaj!)
        Review review = new Review();
        review.setStars(request.stars());
        review.setComment(request.comment());
        review.setReviewDate(LocalDate.now());
        review.setBoardGame(game);

        // 4. Zapis do bazy
        Review savedReview = reviewRepository.save(review);

        // 5. Zwracamy piękny obiekt DTO
        return new ReviewResponse(
                savedReview.getId(),
                savedReview.getStars(),
                savedReview.getComment(),
                savedReview.getReviewDate(),
                game.getTitle()
        );
    }
}