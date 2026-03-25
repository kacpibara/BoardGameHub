package pl.BoardGameHub.api.dto;

public record RentalRequest(
        Long gameId,
        Boolean useLoyaltyPoints
) {}