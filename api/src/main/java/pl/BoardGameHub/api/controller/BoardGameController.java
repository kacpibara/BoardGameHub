package pl.BoardGameHub.api.controller;

import pl.BoardGameHub.api.repository.BoardGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.model.BoardGame;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor // Lombok wstrzyknie repozytorium przez konstruktor
public class BoardGameController {

    private final BoardGameRepository boardGameRepository;

    @GetMapping
    public List<BoardGame> getAllGames() {
        return boardGameRepository.findAll();
    }

    @PostMapping
    public BoardGame addGame(@RequestBody BoardGame game) {
        return boardGameRepository.save(game);
    }
}
