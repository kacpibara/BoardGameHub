package pl.BoardGameHub.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationRequest(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long clientId,
        Long cafeTableId,
        List<Long> gameIds // Lista ID gier, w które chcą zagrać
) {}