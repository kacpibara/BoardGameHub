package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.CafeTableReservationRequest;
import pl.BoardGameHub.api.model.CafeTableReservation;
import pl.BoardGameHub.api.service.CafeTableReservationService;

import java.util.List;

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

    @GetMapping("/my")
    public ResponseEntity<List<CafeTableReservation>> getMyReservations() {
        return ResponseEntity.ok(reservationService.getMyReservations());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reservationService.cancelReservation(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CafeTableReservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(reservationService.updateReservationStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}