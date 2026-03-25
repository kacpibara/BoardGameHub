package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.BoardGameHub.api.dto.ProfileUpdateRequest;
import pl.BoardGameHub.api.model.Client;
import pl.BoardGameHub.api.model.User;
import pl.BoardGameHub.api.repository.ClientRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @GetMapping("/me")
    public ResponseEntity<Client> getMyData() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta")));
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta"));

        client.setFirstName(request.firstName());
        client.setLastName(request.lastName());

        client.setPhoneNumbers(request.phoneNumbers());

        clientRepository.save(client);
        return ResponseEntity.ok("Profil został pomyślnie zaktualizowany!");
    }
}
