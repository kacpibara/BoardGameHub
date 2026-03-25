package pl.BoardGameHub.api.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.BoardGameHub.api.model.Rental;
import pl.BoardGameHub.api.model.RentalStatus;
import pl.BoardGameHub.api.repository.RentalRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RentalScheduler {

    private final RentalRepository rentalRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkLateRentals() {
        log.info("Robot uruchomiony: Skanowanie bazy w poszukiwaniu spóźnialskich...");

        LocalDate today = LocalDate.now();

        List<Rental> lateRentals = rentalRepository.findByStatusAndDueDateBefore(RentalStatus.ACTIVE, today);

        if (lateRentals.isEmpty()) {
            log.info("Wszyscy oddali gry na czas. Brak spóźnień!");
            return;
        }

        for (Rental rental : lateRentals) {
            rental.setStatus(RentalStatus.LATE);
        }

        rentalRepository.saveAll(lateRentals);
        log.warn("🚨 Uwaga! Oznaczono {} wypożyczeń jako LATE (Po terminie).", lateRentals.size());
    }
}