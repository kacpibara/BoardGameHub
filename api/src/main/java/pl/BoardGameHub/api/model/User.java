package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "users") // "user" - słowo zastrzeżone w wielu bazach
@Inheritance(strategy = InheritanceType.JOINED) // Tworzy osobne tabele połączone kluczem
@Data
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @ElementCollection // Atrybut powtarzalny
    private List<String> phoneNumbers;
}
