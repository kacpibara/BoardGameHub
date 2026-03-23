package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.BoardGameHub.api.model.Reservation;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    // Zwraca TRUE, jeśli istnieje jakakolwiek nieanulowana rezerwacja dla danego stolika, która nakłada się czasowo
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.cafeTable.id = :tableId " +
            "AND r.status != 'CANCELLED' " +
            "AND (r.startTime < :endTime AND r.endTime > :startTime)")
    boolean isTableAlreadyBooked(
            @Param("tableId") Long tableId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
