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

import java.time.LocalDate;
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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Klient nie znaleziony"));

        CafeTable table = cafeTableRepository.findById(request.tableId())
                .orElseThrow(() -> new EntityNotFoundException("Stolik nie znaleziony"));

        List<CafeTableReservation> existingReservations = reservationRepository
                .findByCafeTableIdAndReservationDateAndStatus(table.getId(), request.reservationDate(), "ACTIVE");

        LocalTime newStart = request.startTime();
        LocalTime newEnd = newStart.plusHours(request.durationHours());

        for (CafeTableReservation res : existingReservations) {
            LocalTime existingStart = res.getStartTime();
            LocalTime existingEnd = existingStart.plusHours(res.getDurationHours());

            if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                throw new IllegalStateException("Niestety, ten stolik jest już zajęty w godzinach od "
                        + existingStart + " do " + existingEnd + ".");
            }
        }

        CafeTableReservation reservation = new CafeTableReservation();
        reservation.setClient(client);
        reservation.setCafeTable(table);
        reservation.setReservationDate(request.reservationDate());
        reservation.setStartTime(request.startTime());
        reservation.setDurationHours(request.durationHours());
        reservation.setStatus("ACTIVE");

        if (request.optionalGameId() != null) {
            BoardGame game = boardGameRepository.findById(request.optionalGameId())
                    .orElseThrow(() -> new EntityNotFoundException("Gra nie znaleziona"));

            if (game.getTotalCopies() <= 0) {
                throw new IllegalStateException("Niestety, ta gra nie jest w tym momencie dostępna.");
            }

            game.setTotalCopies(game.getTotalCopies() - 1);
            boardGameRepository.save(game);
            reservation.setBoardGame(game);
        }

        reservationRepository.save(reservation);

        return reservation.getBoardGame() != null
                ? "Zarezerwowano stolik nr " + table.getTableNumber() + " wraz z grą " + reservation.getBoardGame().getTitle() + "!"
                : "Zarezerwowano sam stolik nr " + table.getTableNumber() + "!";
    }

    public List<CafeTableReservation> getMyReservations() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return reservationRepository.findByClientEmailOrderByReservationDateDesc(email);
    }

    @Transactional
    public String cancelReservation(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CafeTableReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rezerwacji"));

        if (!reservation.getClient().getEmail().equals(email)) {
            throw new IllegalStateException("Nie możesz anulować cudzej rezerwacji!");
        }

        if (!reservation.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("Rezerwacja nie jest aktywna.");
        }

        reservation.setStatus("CANCELLED");

        if (reservation.getBoardGame() != null) {
            BoardGame game = reservation.getBoardGame();
            game.setTotalCopies(game.getTotalCopies() + 1);
            boardGameRepository.save(game);
        }

        reservationRepository.save(reservation);
        return "Rezerwacja stolika została pomyślnie anulowana.";
    }

    public List<CafeTableReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public String updateReservationStatus(Long id, String newStatus) {
        CafeTableReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rezerwacji"));

        if (!reservation.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("Można edytować tylko aktywne rezerwacje.");
        }

        reservation.setStatus(newStatus);

        if (reservation.getBoardGame() != null) {
            BoardGame game = reservation.getBoardGame();
            game.setTotalCopies(game.getTotalCopies() + 1);
            boardGameRepository.save(game);
        }

        reservationRepository.save(reservation);
        return "Status rezerwacji zaktualizowany na: " + newStatus;
    }


}