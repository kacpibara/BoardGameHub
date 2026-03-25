package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.BoardGameHub.api.model.BoardGame;

import java.util.List;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    List<BoardGame> findByCategoryIgnoreCase(String category);

    @Query("SELECT b FROM BoardGame b WHERE b.minPlayers <= :players AND b.maxPlayers >= :players")
    List<BoardGame> findByPlayerCount(@Param("players") int players);
}
