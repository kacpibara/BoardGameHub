package pl.BoardGameHub.api.dto;

import java.time.LocalDate;

public record RentalResponse(
        Long id,
        String clientFullName,
        String gameTitle,
        LocalDate rentalDate,
        LocalDate dueDate,
        String status
) {}