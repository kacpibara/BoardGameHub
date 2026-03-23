package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.BoardGameHub.api.model.Client;
import pl.BoardGameHub.api.repository.ClientRepository;

@RestController
@RequestMapping("/api/clients") // To musi pasować do adresu w Vue!
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @GetMapping("/me")
    public ResponseEntity<Client> getMyData() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta")));
    }
}
