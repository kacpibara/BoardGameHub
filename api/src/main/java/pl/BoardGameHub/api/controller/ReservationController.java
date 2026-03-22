package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.model.Reservation;
import pl.BoardGameHub.api.repository.ReservationRepository;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepository;

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        // Zapisujemy rezerwację, a dzięki kaskadzie zapisze się też cała reszta!
        return reservationRepository.save(reservation);
    }
}
