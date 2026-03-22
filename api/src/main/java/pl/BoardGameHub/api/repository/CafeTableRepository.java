package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.CafeTable;

public interface CafeTableRepository extends JpaRepository<CafeTable, Long> {}

