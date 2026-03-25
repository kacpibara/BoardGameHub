package pl.BoardGameHub.api.dto;
import java.util.List;

public record ProfileUpdateRequest(
        String firstName,
        String lastName,
        List<String> phoneNumbers
) {}