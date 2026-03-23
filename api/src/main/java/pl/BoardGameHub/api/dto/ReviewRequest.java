package pl.BoardGameHub.api.dto;

public record ReviewRequest(
        int stars,
        String comment,
        Long gameId
) {}