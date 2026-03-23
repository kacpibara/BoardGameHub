package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.RentalRequest;
import pl.BoardGameHub.api.dto.RentalResponse;
import pl.BoardGameHub.api.service.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;


    @GetMapping("/my")
    public ResponseEntity<List<RentalResponse>> getMyRentals() {
        return ResponseEntity.ok(rentalService.getMyRentals());
    }

    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@RequestBody RentalRequest request) {
        return ResponseEntity.ok(rentalService.createRental(request));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<String> returnGame(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.returnGame(id));
    }

}