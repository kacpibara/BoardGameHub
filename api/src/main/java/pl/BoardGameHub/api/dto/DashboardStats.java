package pl.BoardGameHub.api.dto;

import java.util.Map;

public record DashboardStats(
        long totalGames,
        long activeRentals,
        double totalPenalties,
        Map<String, Long> rentalsByCategory
) {}