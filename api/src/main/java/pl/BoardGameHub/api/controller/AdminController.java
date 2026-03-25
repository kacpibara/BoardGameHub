package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.BoardGameHub.api.dto.DashboardStats;
import pl.BoardGameHub.api.model.Rental;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.RentalRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BoardGameRepository boardGameRepository;
    private final RentalRepository rentalRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        List<Rental> allRentals = rentalRepository.findAll();

        long totalGames = boardGameRepository.count();
        long activeRentals = allRentals.stream().filter(r -> r.getStatus().name().equals("ACTIVE")).count();

        double totalPenalties = allRentals.stream()
                .filter(r -> r.getPenaltyFee() != null)
                .mapToDouble(Rental::getPenaltyFee)
                .sum();

        Map<String, Long> byCategory = allRentals.stream()
                .filter(r -> r.getBoardGame() != null && r.getBoardGame().getCategory() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getBoardGame().getCategory(),
                        Collectors.counting()
                ));

        return ResponseEntity.ok(new DashboardStats(totalGames, activeRentals, totalPenalties, byCategory));
    }
}