package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.CafeTableReservationRequest;
import pl.BoardGameHub.api.service.CafeTableReservationService;

@RestController
@RequestMapping("/api/table-reservations")
@RequiredArgsConstructor
public class CafeTableReservationController {

    private final CafeTableReservationService reservationService;

    @PostMapping
    public ResponseEntity<String> reserveTable(@RequestBody CafeTableReservationRequest request) {
        try {
            return ResponseEntity.ok(reservationService.createReservation(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}