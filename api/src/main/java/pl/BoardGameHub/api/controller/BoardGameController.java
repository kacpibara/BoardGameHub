package pl.BoardGameHub.api.controller;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.BoardGameRequest;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.service.BoardGameService;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor // Lombok wstrzyknie repozytorium przez konstruktor
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final BoardGameRepository boardGameRepository;

    // Pobieranie wszystkich gier LUB filtrowanie (np. /api/games?category=Strategiczna)
    @GetMapping
    public ResponseEntity<List<BoardGame>> getAllGames(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer players) {
        return ResponseEntity.ok(boardGameService.getGames(category, players));
    }

    // Pobieranie jednej konkretnej gry po jej ID (np. /api/games/1)
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
