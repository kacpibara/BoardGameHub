package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class CafeTableReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "cafe_table_id")
    private CafeTable cafeTable;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private BoardGame boardGame;

    private LocalDate reservationDate;
    private LocalTime startTime;
    private int durationHours;

    private String status;
}