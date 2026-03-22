package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.Rental;

public interface RentalRepository extends JpaRepository<Rental,Long> {
}
