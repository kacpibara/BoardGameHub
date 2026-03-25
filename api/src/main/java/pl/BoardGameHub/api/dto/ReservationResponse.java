package pl.BoardGameHub.api.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status,
        long durationInHours,
        String clientFullName,
        int tableNumber
) {}