package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
