package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.ReviewRequest;
import pl.BoardGameHub.api.dto.ReviewResponse;
import pl.BoardGameHub.api.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.addReview(request));
    }
}