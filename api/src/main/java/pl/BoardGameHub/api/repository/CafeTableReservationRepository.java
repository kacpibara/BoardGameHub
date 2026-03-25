package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.CafeTableReservation;

import java.time.LocalDate;
import java.util.List;

public interface CafeTableReservationRepository extends JpaRepository<CafeTableReservation, Long> {
    List<CafeTableReservation> findByCafeTableIdAndReservationDateAndStatus(Long cafeTableId, LocalDate reservationDate, String status);
    List<CafeTableReservation> findByClientEmailOrderByReservationDateDesc(String email);

    List<CafeTableReservation> findByStatusAndReservationDateBefore(String status, LocalDate date);


}