package pl.BoardGameHub.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.BoardGameHub.api.dto.auth.AuthenticationRequest;
import pl.BoardGameHub.api.dto.auth.AuthenticationResponse;
import pl.BoardGameHub.api.dto.auth.RegisterRequest;
import pl.BoardGameHub.api.model.Client;
import pl.BoardGameHub.api.model.Role;
import pl.BoardGameHub.api.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository repository;
    private final PasswordEncoder passwordEncoder; // Nasz szyfrator BCrypt
    private final JwtService jwtService; // Magik od tokenów
    private final AuthenticationManager authenticationManager; // Główny weryfikator Springa

    public AuthenticationResponse register(RegisterRequest request) {
        // 1. Tworzymy nowego klienta
        var client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        // BARDZO WAŻNE: Szyfrujemy hasło przed zapisem do bazy!
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setRole(Role.USER);
        client.setLoyaltyPoints(0); // Nowy klient ma 0 punktów

        // 2. Zapisujemy do bazy danych
        repository.save(client);

        // 3. Od razu generujemy mu token, żeby po rejestracji był zalogowany
        var jwtToken = jwtService.generateToken(client);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Próbujemy zalogować. Jeśli hasło jest złe, ta linijka rzuci wyjątkiem i zatrzyma kod!
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Jeśli przeszło, wyciągamy użytkownika z bazy
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        // 3. Generujemy nowy, świeży token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}