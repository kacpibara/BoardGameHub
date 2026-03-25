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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());

        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setRole(Role.USER);
        client.setLoyaltyPoints(0);

        repository.save(client);

        var jwtToken = jwtService.generateToken(client);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}