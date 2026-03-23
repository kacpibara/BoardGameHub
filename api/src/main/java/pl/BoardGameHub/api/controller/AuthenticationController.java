package pl.BoardGameHub.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.BoardGameHub.api.dto.auth.AuthenticationRequest;
import pl.BoardGameHub.api.dto.auth.AuthenticationResponse;
import pl.BoardGameHub.api.dto.auth.RegisterRequest;
import pl.BoardGameHub.api.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth") // Zauważ, że te ścieżki odblokowaliśmy w SecurityConfig!
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}