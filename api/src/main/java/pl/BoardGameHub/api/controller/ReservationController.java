package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.ReservationRequest;
import pl.BoardGameHub.api.dto.ReservationResponse;
import pl.BoardGameHub.api.model.Reservation;
import pl.BoardGameHub.api.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }
}
