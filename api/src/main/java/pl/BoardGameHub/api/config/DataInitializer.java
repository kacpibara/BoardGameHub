package pl.BoardGameHub.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.BoardGameHub.api.model.*;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.CafeTableRepository;
import pl.BoardGameHub.api.repository.ClientRepository;
import pl.BoardGameHub.api.repository.RentalRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final BoardGameRepository boardGameRepository;
    private final CafeTableRepository cafeTableRepository;
    private final ClientRepository clientRepository;
    private final RentalRepository rentalRepository;

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("🔍 Sprawdzanie stanu bazy danych...");

        if (cafeTableRepository.count() == 0) {
            log.info("🪑 Generowanie stolików...");
            CafeTable t1 = new CafeTable(); t1.setTableNumber(1); t1.setNumberOfSeats(2); t1.setLocation("Przy oknie");
            CafeTable t2 = new CafeTable(); t2.setTableNumber(2); t2.setNumberOfSeats(4); t2.setLocation("Kanapa");
            CafeTable t3 = new CafeTable(); t3.setTableNumber(3); t3.setNumberOfSeats(4); t3.setLocation("Środek sali");
            CafeTable t4 = new CafeTable(); t4.setTableNumber(4); t4.setNumberOfSeats(6); t4.setLocation("Duży blat");
            CafeTable t5 = new CafeTable(); t5.setTableNumber(5); t5.setNumberOfSeats(8); t5.setLocation("VIP room");
            cafeTableRepository.saveAll(List.of(t1, t2, t3, t4, t5));
        }

        if (boardGameRepository.count() == 0) {
            log.info("🎲 Generowanie gier, testowych klientów i statystyk...");

            BoardGame g1 = new BoardGame(null, "Catan", "Strategiczne", 3, 4, 15.0, 5, "https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=600");
            BoardGame g2 = new BoardGame(null, "Dixit", "Imprezowe", 3, 6, 12.0, 3, "https://images.unsplash.com/photo-1558236714-d1a6333efc66?q=80&w=600");
            BoardGame g3 = new BoardGame(null, "Wsiąść do Pociągu", "Rodzinne", 2, 5, 20.0, 4, "https://images.unsplash.com/photo-1605806616949-1e87b487cb2a?q=80&w=600");
            BoardGame g4 = new BoardGame(null, "Splendor", "Ekonomiczne", 2, 4, 10.0, 6, "https://images.unsplash.com/photo-1632506828551-789ce8681e19?q=80&w=600");
            BoardGame g5 = new BoardGame(null, "Terraformacja Marsa", "Strategiczne", 1, 5, 25.0, 2, "https://images.unsplash.com/photo-1611843467160-25afb8df1074?q=80&w=600");
            BoardGame g6 = new BoardGame(null, "Dobble", "Imprezowe", 2, 8, 5.0, 10, "https://images.unsplash.com/photo-1606503153255-59d8b8b82176?q=80&w=600");
            BoardGame g7 = new BoardGame(null, "Everdell", "Strategiczne", 1, 4, 22.0, 3, "https://images.unsplash.com/photo-1585504198199-20277593b94f?q=80&w=600");
            boardGameRepository.saveAll(Arrays.asList(g1, g2, g3, g4, g5, g6, g7));

            Client c = new Client();
            c.setEmail("test@test.pl");
            c.setPassword(passwordEncoder.encode("test"));            c.setFirstName("Jan");
            c.setLastName("Testowy");
            c.setRole(Role.USER);
            clientRepository.save(c);

            Rental r1 = new Rental();
            r1.setBoardGame(g1);
            r1.setClient(c);
            r1.setRentalDate(LocalDate.now());
            r1.setDueDate(LocalDate.now().plusDays(3));
            r1.setStatus(RentalStatus.ACTIVE);
            r1.setPenaltyFee(0.0);

            Rental r2 = new Rental();
            r2.setBoardGame(g1);
            r2.setClient(c);
            r2.setRentalDate(LocalDate.now().minusDays(20));
            r2.setDueDate(LocalDate.now().minusDays(13));
            r2.setStatus(RentalStatus.LATE);
            r2.setPenaltyFee(40.0);

            Rental r3 = new Rental();
            r3.setBoardGame(g2);
            r3.setClient(c);
            r3.setRentalDate(LocalDate.now().minusDays(15));
            r3.setDueDate(LocalDate.now().minusDays(8));
            r3.setStatus(RentalStatus.LATE);
            r3.setPenaltyFee(30.0);

            rentalRepository.saveAll(Arrays.asList(r1, r2, r3));
        }
    }
}