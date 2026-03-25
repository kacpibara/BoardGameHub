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
        if (request.stars() < 1 || request.stars() > 5) {
            throw new IllegalStateException("Ocena musi wynosić od 1 do 5 gwiazdek!");
        }

        BoardGame game = boardGameRepository.findById(request.gameId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono gry o podanym ID!"));

        Review review = new Review();
        review.setStars(request.stars());
        review.setComment(request.comment());
        review.setReviewDate(LocalDate.now());
        review.setBoardGame(game);

        Review savedReview = reviewRepository.save(review);

        return new ReviewResponse(
                savedReview.getId(),
                savedReview.getStars(),
                savedReview.getComment(),
                savedReview.getReviewDate(),
                savedReview.getBoardGame().getTitle(),
                game.getTitle()
        );
    }
}