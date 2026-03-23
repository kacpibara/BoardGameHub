package pl.BoardGameHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.BoardGameHub.api.model.Client;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Spring Data JPA sam napisze zapytanie SQL na podstawie nazwy tej metody!
    Optional<Client> findByEmail(String email);

}