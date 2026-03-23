package pl.BoardGameHub.api.dto;

import java.time.LocalDate;

public record ReviewResponse(
        Long id,
        int stars,
        String comment,
        LocalDate reviewDate,
        String gameTitle
) {}