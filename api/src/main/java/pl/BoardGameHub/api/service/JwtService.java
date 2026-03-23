package pl.BoardGameHub.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // To jest nasz TAJNY KLUCZ. Służy do podpisywania tokenów.
    // W prawdziwym projekcie trzyma się go w application.properties, ale do nauki wpisujemy go tutaj na sztywno.
    // UWAGA: Musi mieć co najmniej 256 bitów (to jest po prostu długi, losowy tekst w formacie Base64)
    private static final String SECRET_KEY = "ZGF2aWRib3dpZWlzYWhlcm9hbmR0aGlzaXNteXN1cGVyc2VjcmV0a2V5MTIzNDU2Nzg5MA==";

    // 1. Wyciąganie emaila (username) z tokenu, który przyjdzie z frontendu
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Metoda do tworzenia nowiutkiego tokena po udanym logowaniu
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Magia generowania: ustalamy kto, kiedy i do kiedy ma token
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // Wrzucamy email jako główny identyfikator
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data wydania: TERAZ
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 24)) // Ważny przez 24 godziny
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Podpisujemy naszym super tajnym kluczem
                .compact();
    }

    // 3. Sprawdzanie, czy token jest prawidłowy i czy nie wygasł
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}