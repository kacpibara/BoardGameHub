package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING) // zapis w bazie tekstu (np. "PENDING") zamiast cyfry
    private ReservationStatus status;

    // Relacja 1:N - Wiele rezerwacji do jednego klienta
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    // Relacja 1:N - Wiele rezerwacji do jednego stolika
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_id")
    private CafeTable cafeTable;

    // Relacja M:N - Rezerwacja może mieć wiele gier, a gra być w wielu rezerwacjach
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "reservation_games", // Nazwa automatycznie stworzonej tabeli łączącej
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<BoardGame> requestedGames;

    // Atrybut pochodny /DurationInHours
    public long getDurationInHours() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toHours();
        }
        return 0;
    }
}
