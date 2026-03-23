package pl.BoardGameHub.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.BoardGameHub.api.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Sprawdzamy, czy w zapytaniu HTTP przyszedł nagłówek "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Jeśli nagłówka nie ma, albo nie zaczyna się od słowa "Bearer " (branżowy standard), to przepuszczamy zapytanie dalej (być może to próba logowania, która nie wymaga tokena)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Jeśli jest token, to odcinamy pierwsze 7 znaków ("Bearer ") i wyciągamy sam ciąg znaków JWT
        jwt = authHeader.substring(7);

        // 3. Nasz "Magik" wyciąga email (username) z tokena
        userEmail = jwtService.extractUsername(jwt);

        // 4. Jeśli w tokenie jest email, a w aktualnym kontekście Springa użytkownik NIE JEST jeszcze zalogowany...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Szukamy użytkownika w bazie danych (na podstawie wyciągniętego emaila)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Sprawdzamy, czy token na pewno należy do tego użytkownika i czy nie wygasł
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Jeśli wszystko gra, ręcznie "logujemy" użytkownika w pamięci Springa na czas trwania tego konkretnego zapytania!
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Przepuszczamy zapytanie dalej, do właściwego Kontrolera (np. od Rezerwacji)
        filterChain.doFilter(request, response);
    }
}