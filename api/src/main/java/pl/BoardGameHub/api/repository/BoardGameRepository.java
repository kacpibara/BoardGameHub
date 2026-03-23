package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.BoardGameHub.api.model.BoardGame;

import java.util.List;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    // Spring sam zgadnie, że ma szukać po kolumnie 'category' ignorując wielkość liter!
    List<BoardGame> findByCategoryIgnoreCase(String category);

    // Szukamy gier, w które może zagrać konkretna liczba osób
    @Query("SELECT b FROM BoardGame b WHERE b.minPlayers <= :players AND b.maxPlayers >= :players")
    List<BoardGame> findByPlayerCount(@Param("players") int players);
}
