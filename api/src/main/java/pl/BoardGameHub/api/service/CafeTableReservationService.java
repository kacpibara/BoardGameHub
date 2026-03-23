package pl.BoardGameHub.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.BoardGameHub.api.dto.CafeTableReservationRequest;
import pl.BoardGameHub.api.dto.CafeTableReservationRequest;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.model.CafeTable;
import pl.BoardGameHub.api.model.Client;
import pl.BoardGameHub.api.model.CafeTableReservation;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.CafeTableRepository;
import pl.BoardGameHub.api.repository.ClientRepository;
import pl.BoardGameHub.api.repository.CafeTableReservationRepository;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeTableReservationService {

    private final CafeTableReservationRepository reservationRepository;
    private final CafeTableRepository cafeTableRepository;
    private final BoardGameRepository boardGameRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public String createReservation(CafeTableReservationRequest request) {
        // 1. Pobierz zalogowanego klienta
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Klient nie znaleziony"));

        // 2. Pobierz stolik
        CafeTable table = cafeTableRepository.findById(request.tableId())
                .orElseThrow(() -> new EntityNotFoundException("Stolik nie znaleziony"));

        List<CafeTableReservation> existingReservations = reservationRepository
                .findByCafeTableIdAndReservationDateAndStatus(table.getId(), request.reservationDate(), "ACTIVE");

        LocalTime newStart = request.startTime();
        LocalTime newEnd = newStart.plusHours(request.durationHours());

        for (CafeTableReservation res : existingReservations) {
            LocalTime existingStart = res.getStartTime();
            LocalTime existingEnd = existingStart.plusHours(res.getDurationHours());

            // Sprawdzamy, czy przedziały czasowe na siebie nachodzą
            if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                throw new IllegalStateException("Niestety, ten stolik jest już zajęty w godzinach od "
                        + existingStart + " do " + existingEnd + ".");
            }
        }

        // 3. Tworzymy rezerwację
        CafeTableReservation reservation = new CafeTableReservation();
        reservation.setClient(client);
        reservation.setCafeTable(table);
        reservation.setReservationDate(request.reservationDate());
        reservation.setStartTime(request.startTime());
        reservation.setDurationHours(request.durationHours());
        reservation.setStatus("ACTIVE");

        // 4. Jeśli podano grę (Extended Version!)
        if (request.optionalGameId() != null) {
            BoardGame game = boardGameRepository.findById(request.optionalGameId())
                    .orElseThrow(() -> new EntityNotFoundException("Gra nie znaleziona"));

            if (game.getTotalCopies() <= 0) {
                throw new IllegalStateException("Niestety, ta gra nie jest w tym momencie dostępna.");
            }

            // Odkładamy grę dla klienta
            game.setTotalCopies(game.getTotalCopies() - 1);
            boardGameRepository.save(game);
            reservation.setBoardGame(game);
        }

        reservationRepository.save(reservation);

        return reservation.getBoardGame() != null
                ? "Zarezerwowano stolik nr " + table.getTableNumber() + " wraz z grą " + reservation.getBoardGame().getTitle() + "!"
                : "Zarezerwowano sam stolik nr " + table.getTableNumber() + "!";
    }
}