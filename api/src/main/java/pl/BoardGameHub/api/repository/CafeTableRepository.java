package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.CafeTable;
import pl.BoardGameHub.api.model.CafeTableReservation;

import java.util.List;

public interface CafeTableRepository extends JpaRepository<CafeTable, Long> {
}

