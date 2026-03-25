package pl.BoardGameHub.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CafeTableReservationRequest(
        Long tableId,
        LocalDate reservationDate,
        LocalTime startTime,
        int durationHours,
        Long optionalGameId
) {}