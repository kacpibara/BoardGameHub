package pl.BoardGameHub.api.controller;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.BoardGameRequest;
import pl.BoardGameHub.api.dto.GameListResponse;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.ReviewRepository;
import pl.BoardGameHub.api.service.BoardGameService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final BoardGameRepository boardGameRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<List<GameListResponse>> getAllGames() {
        List<BoardGame> games = boardGameRepository.findAll();

        List<GameListResponse> response = games.stream()
                .map(game -> {
                    var reviews = reviewRepository.findByBoardGameId(game.getId());

                    Double avg = reviews.stream()
                            .mapToDouble(pl.BoardGameHub.api.model.Review::getStars)
                            .average()
                            .orElse(0.0);

                    return new GameListResponse(game, avg, (long) reviews.size());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> getGameById(@PathVariable Long id) {
        return ResponseEntity.ok(boardGameService.getGameById(id));
    }

    @PostMapping
    public ResponseEntity<String> addGame(@RequestBody BoardGameRequest request) {
        BoardGame game = new BoardGame();
        game.setTitle(request.title());
        game.setCategory(request.category());
        game.setMinPlayers(request.minPlayers());
        game.setMaxPlayers(request.maxPlayers());
        game.setRentalPrice(request.rentalPrice());
        game.setTotalCopies(request.totalCopies());
        game.setImageUrl(request.imageUrl());

        boardGameRepository.save(game);
        return ResponseEntity.ok("Gra " + game.getTitle() + " została pomyślnie dodana do katalogu!");
    }
}
