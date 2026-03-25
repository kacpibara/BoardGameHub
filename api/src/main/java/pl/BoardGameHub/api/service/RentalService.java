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
import java.time.temporal.ChronoUnit;
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

        double finalDeposit = game.getRentalPrice() * 2;
        boolean usedPoints = request.useLoyaltyPoints() != null && request.useLoyaltyPoints();

        if (usedPoints) {
            if (client.getLoyaltyPoints() < 50) {
                throw new IllegalStateException("Nie masz wystarczającej liczby punktów (wymagane 50).");
            }

            client.setLoyaltyPoints(client.getLoyaltyPoints() - 50);
            finalDeposit = 0.0;
        } else {
            client.setLoyaltyPoints(client.getLoyaltyPoints() + 10);
        }
        clientRepository.save(client);

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
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia!"));

        if (rental.getStatus() == RentalStatus.RETURNED) {
            return "Gra została już zwrócona.";
        }

        LocalDate today = LocalDate.now();
        if (today.isAfter(rental.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(rental.getDueDate(), today);
            double penalty = daysLate * 5.0;
            rental.setPenaltyFee(penalty);
        } else {
            rental.setPenaltyFee(0.0);
        }

        rental.setStatus(RentalStatus.RETURNED);

        BoardGame game = rental.getBoardGame();
        game.setTotalCopies(game.getTotalCopies() + 1);
        boardGameRepository.save(game);

        rentalRepository.save(rental);

        if (rental.getPenaltyFee() > 0) {
            return "Gra zwrócona PO TERMINIE! Do zapłaty kara: " + rental.getPenaltyFee() + " zł.";
        }
        return "Gra zwrócona pomyślnie w terminie. Brak kar.";
    }

}