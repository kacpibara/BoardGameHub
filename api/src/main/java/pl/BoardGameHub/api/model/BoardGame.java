package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private int minPlayers;
    private int maxPlayers;
    private double rentalPrice;
    private int totalCopies;

    private String imageUrl;

    public boolean isAvailable() {
        return totalCopies > 0;
    }
}
