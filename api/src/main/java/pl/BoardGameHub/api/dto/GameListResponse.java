package pl.BoardGameHub.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.BoardGameHub.api.model.BoardGame;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameListResponse {
    private BoardGame game;
    private Double averageStars;
    private Long reviewsCount;
}