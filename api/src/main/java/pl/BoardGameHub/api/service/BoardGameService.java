package pl.BoardGameHub.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.repository.BoardGameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    public List<BoardGame> getGames(String category, Integer players) {
        if (category != null) {
            return boardGameRepository.findByCategoryIgnoreCase(category);
        }
        if (players != null) {
            return boardGameRepository.findByPlayerCount(players);
        }
        // Jeśli klient nie podał filtrów, zwracamy wszystko
        return boardGameRepository.findAll();
    }

    public BoardGame getGameById(Long id) {
        return boardGameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono gry o ID: " + id));
    }
}