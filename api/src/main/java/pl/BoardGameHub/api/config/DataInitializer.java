package pl.BoardGameHub.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.BoardGameHub.api.model.*;
import pl.BoardGameHub.api.repository.*;

import java.util.List;

@Component // Mówi Springowi: "Zarządzaj tą klasą"
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

//    private final BoardGameRepository boardGameRepository;
//    private final CafeTableRepository cafeTableRepository;
//    private final ClientRepository clientRepository;

//    @Override
//    public void run(String... args) throws Exception {
//        // Ta metoda uruchamia się automatycznie po starcie aplikacji!
//
//        // 1. Tworzymy Stolik (ID 1)
//        CafeTable table = new CafeTable();
//        table.setTableNumber(5);
//        table.setNumberOfSeats(4);
//        table.setLocation("Strefa VIP");
//        cafeTableRepository.save(table);
//
//        // 2. Tworzymy Klienta (ID 1)
//        Client client = new Client();
//        client.setFirstName("Jan");
//        client.setLastName("Planszówkowy");
//        client.setEmail("jan@pawsandplay.pl");
//        client.setPhoneNumbers(List.of("123456789"));
//        client.setLoyaltyPoints(150);
//        clientRepository.save(client);

            // DODAJ TO WSTRZYKNIĘCIE:
            private final CafeTableRepository cafeTableRepository;
            // ... (Twoje inne repozytoria)

            @Override
            public void run(String... args) throws Exception {

                // Zabezpieczenie: dodajemy stoliki TYLKO, jeśli tabela jest pusta
                if (cafeTableRepository.count() == 0) {
                    CafeTable t1 = new CafeTable(); t1.setTableNumber(1); t1.setNumberOfSeats(2); t1.setLocation("Przy oknie");
                    CafeTable t2 = new CafeTable(); t2.setTableNumber(2); t2.setNumberOfSeats(4); t2.setLocation("Kanapa");
                    CafeTable t3 = new CafeTable(); t3.setTableNumber(3); t3.setNumberOfSeats(4); t3.setLocation("Środek sali");
                    CafeTable t4 = new CafeTable(); t4.setTableNumber(4); t4.setNumberOfSeats(6); t4.setLocation("Duży blat");
                    CafeTable t5 = new CafeTable(); t5.setTableNumber(5); t5.setNumberOfSeats(8); t5.setLocation("VIP room");

                    // Zapisujemy wszystkie naraz!
                    cafeTableRepository.saveAll(List.of(t1, t2, t3, t4, t5));
                    System.out.println("✅ Stoliki wygenerowane pomyślnie!");
                }

                // ... (Tu masz swój stary kod od dodawania użytkownika Jan,
                // pamiętaj, żeby on też miał IFa sprawdzającego, czy Jan już nie istnieje!)
            }
        }
//
//        // 3. Tworzymy Gry (ID 1 i ID 2)
//        BoardGame catan = new BoardGame();
//        catan.setTitle("Catan");
//        catan.setCategory("Strategiczna");
//        catan.setMinPlayers(3);
//        catan.setMaxPlayers(4);
//        catan.setRentalPrice(15.0);
//        catan.setTotalCopies(2);
//
//        BoardGame dixit = new BoardGame();
//        dixit.setTitle("Dixit");
//        dixit.setCategory("Imprezowa");
//        dixit.setMinPlayers(3);
//        dixit.setMaxPlayers(6);
//        dixit.setRentalPrice(10.0);
//        dixit.setTotalCopies(3);
//
//        boardGameRepository.saveAll(List.of(catan, dixit));
//
//        System.out.println("✅ Baza PostgresSQL gotowa do akcji!");
//    }
//}