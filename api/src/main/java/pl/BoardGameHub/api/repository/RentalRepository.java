package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.Rental;
import pl.BoardGameHub.api.model.RentalStatus;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental,Long> {
    List<Rental> findByStatusAndDueDateBefore(RentalStatus status, LocalDate date);
}
