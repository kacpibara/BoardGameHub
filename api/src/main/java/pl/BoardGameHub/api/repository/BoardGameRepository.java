package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.BoardGame;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {}
