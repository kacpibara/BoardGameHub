package pl.BoardGameHub.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.BoardGameHub.api.dto.RentalRequest;
import pl.BoardGameHub.api.dto.RentalResponse;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.model.Client;
import pl.BoardGameHub.api.model.Rental;
import pl.BoardGameHub.api.model.RentalStatus;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.ClientRepository;
import pl.BoardGameHub.api.repository.RentalRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final ClientRepository clientRepository;
    private final BoardGameRepository boardGameRepository;

    @Transactional
    public RentalResponse createRental(RentalRequest request) {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono klienta!"));

        BoardGame game = boardGameRepository.findById(request.gameId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono gry!"));

        if (game.getTotalCopies() <= 0) {
            throw new IllegalStateException("Brak wolnych kopii.");
        }

        // --- NOWA LOGIKA PUNKTÓW ---
        client.setLoyaltyPoints(client.getLoyaltyPoints() + 10);
        clientRepository.save(client); // Zapisujemy nowe punkty klienta
        // ---------------------------

        game.setTotalCopies(game.getTotalCopies() - 1);
        boardGameRepository.save(game);

        Rental rental = new Rental();
        rental.setClient(client);
        rental.setBoardGame(game);
        rental.setRentalDate(LocalDate.now());
        rental.setDueDate(LocalDate.now().plusDays(7));
        rental.setStatus(RentalStatus.ACTIVE);
        rental.setDepositAmount(game.getRentalPrice() * 2);

        Rental savedRental = rentalRepository.save(rental);

        return new RentalResponse(
                savedRental.getId(),
                client.getFirstName() + " " + client.getLastName(),
                game.getTitle(),
                savedRental.getRentalDate(),
                savedRental.getDueDate(),
                savedRental.getStatus().name()
        );
    }

    public List<RentalResponse> getMyRentals() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // Zakładamy, że dodasz metodę findByClientEmail w RentalRepository lub przefiltrujesz tutaj
        return rentalRepository.findAll().stream()
                .filter(r -> r.getClient().getEmail().equals(email))
                .map(r -> new RentalResponse(
                        r.getId(),
                        r.getClient().getFirstName() + " " + r.getClient().getLastName(),
                        r.getBoardGame().getTitle(),
                        r.getRentalDate(),
                        r.getDueDate(),
                        r.getStatus().name()
                ))
                .toList();
    }

    @Transactional
    public String returnGame(Long rentalId) {
        // 1. Szukamy wypożyczenia
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono wypożyczenia!"));

        if (rental.getStatus() == RentalStatus.RETURNED) {
            return "Ta gra została już oddana.";
        }

        // 2. Zmieniamy status wypożyczenia
        rental.setStatus(RentalStatus.RETURNED);

        // 3. Zwiększamy ilość dostępnych sztuk gry
        BoardGame game = rental.getBoardGame();
        game.setTotalCopies(game.getTotalCopies() + 1);

        rentalRepository.save(rental);
        boardGameRepository.save(game);

        return "Dziękujemy za zwrot gry: " + game.getTitle();
    }

}