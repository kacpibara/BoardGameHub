package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stars;
    private String comment;
    private LocalDate reviewDate;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private BoardGame boardGame;
}
