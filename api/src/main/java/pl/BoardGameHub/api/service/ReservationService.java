package pl.BoardGameHub.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.BoardGameHub.api.dto.ReservationRequest;
import pl.BoardGameHub.api.dto.ReservationResponse;
import pl.BoardGameHub.api.model.*;
import pl.BoardGameHub.api.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CafeTableRepository cafeTableRepository;
    private final ClientRepository clientRepository;
    private final BoardGameRepository boardGameRepository;

    public ReservationResponse createReservation(ReservationRequest request) {

        // 1. Wyciągamy obiekty z bazy na podstawie przesłanych ID
        CafeTable table = cafeTableRepository.findById(request.cafeTableId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono stolika!"));

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono klienta!"));

        List<BoardGame> games = boardGameRepository.findAllById(request.gameIds());

        // 2. Walidacja czasowa (tak jak mieliśmy)
        boolean isBooked = reservationRepository.isTableAlreadyBooked(
                table.getId(), request.startTime(), request.endTime()
        );
        if (isBooked) {
            throw new IllegalStateException("Stolik jest zajęty w tym terminie.");
        }

        // 3. Budujemy prawdziwą Encję do zapisu w bazie
        Reservation reservation = new Reservation();
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setStatus(ReservationStatus.PENDING); // Domyślny status
        reservation.setCafeTable(table);
        reservation.setClient(client);
        reservation.setRequestedGames(games);

        // 4. Zapisujemy w bazie
        Reservation saved = reservationRepository.save(reservation);

        // 5. Zmieniamy zapisaną encję na ładny Response DTO
        return new ReservationResponse(
                saved.getId(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus().name(),
                saved.getDurationInHours(),
                client.getFirstName() + " " + client.getLastName(),
                table.getTableNumber()
        );
    }
}