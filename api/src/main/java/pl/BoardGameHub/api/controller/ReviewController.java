package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.ReviewResponse;
import pl.BoardGameHub.api.model.*;
import pl.BoardGameHub.api.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final BoardGameRepository boardGameRepository;

    @PostMapping
    public ResponseEntity<String> addReview(@RequestBody Map<String, String> payload) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email).orElseThrow();
        BoardGame game = boardGameRepository.findById(Long.valueOf(payload.get("gameId"))).orElseThrow();

        Review review = new Review();
        review.setClient(client);
        review.setBoardGame(game);
        review.setStars(Integer.parseInt(payload.get("stars")));
        review.setComment(payload.get("comment"));
        review.setReviewDate(LocalDate.now());

        reviewRepository.save(review);
        return ResponseEntity.ok("Dziękujemy za ocenę gry!");
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<ReviewResponse>> getGameReviews(@PathVariable Long gameId) {
        List<ReviewResponse> reviews = reviewRepository.findByBoardGameId(gameId)
                .stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getStars(),
                        r.getComment(),
                        r.getReviewDate(),
                        r.getBoardGame().getTitle(),
                        r.getClient().getEmail().split("@")[0]
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviews);
    }

}