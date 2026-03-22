package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CafeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;
    private int numberOfSeats;
    private String location;
}
