package pl.BoardGameHub.api.dto;

public record BoardGameRequest(
        String title,
        String category,
        int minPlayers,
        int maxPlayers,
        double rentalPrice,
        int totalCopies,
        String imageUrl
) {}