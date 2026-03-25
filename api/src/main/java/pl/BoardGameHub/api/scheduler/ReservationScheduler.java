package pl.BoardGameHub.api.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.BoardGameHub.api.model.BoardGame;
import pl.BoardGameHub.api.model.CafeTableReservation;
import pl.BoardGameHub.api.repository.BoardGameRepository;
import pl.BoardGameHub.api.repository.CafeTableReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationScheduler {

    private final CafeTableReservationRepository reservationRepository;
    private final BoardGameRepository boardGameRepository;

    @Scheduled(fixedRate = 60000000)
    @Transactional
    public void cleanUpOldReservations() {
        LocalDate today = LocalDate.now();

        List<CafeTableReservation> overdueReservations = reservationRepository
                .findByStatusAndReservationDateBefore("ACTIVE", today);

        if (overdueReservations.isEmpty()) {
            return;
        }

        log.info("Nocne sprzątanie: Znaleziono {} zapomnianych rezerwacji stolików.", overdueReservations.size());

        for (CafeTableReservation res : overdueReservations) {
            res.setStatus("AUTO_CANCELLED");

            if (res.getBoardGame() != null) {
                BoardGame game = res.getBoardGame();
                game.setTotalCopies(game.getTotalCopies() + 1);
                boardGameRepository.save(game);
                log.info("🎲 Zwrócono grę '{}' na półkę ze stolika nr {}.", game.getTitle(), res.getCafeTable().getTableNumber());
            }
        }

        reservationRepository.saveAll(overdueReservations);
    }
}