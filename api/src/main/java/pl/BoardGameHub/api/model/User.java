package pl.BoardGameHub.api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users") // "user" - słowo zastrzeżone w wielu bazach
@Inheritance(strategy = InheritanceType.JOINED) // Tworzy osobne tabele połączone kluczem
@Data
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique = true) // Email musi być unikalny do logowania!
    private String email;

    @ElementCollection // Atrybut powtarzalny
    private List<String> phoneNumbers;

    private String password; // NOWE POLE

    @Enumerated(EnumType.STRING)
    private Role role; // NOWE POLE

    // --- METODY WYMAGANE PRZEZ INTERFEJS UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Tłumaczymy naszą Rolę na format zrozumiały dla Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // W naszym systemie loginem jest EMAIL
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
