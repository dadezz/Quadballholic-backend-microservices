package com.quadballholic.backend.match.scheduler; // Cambia package

import com.quadballholic.backend.match.client.LiveEventClient;
import com.quadballholic.backend.match.entity.MatchEntity;
import com.quadballholic.backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimulationScheduler {

    private final MatchRepository matchRepository;
    private final LiveEventClient liveEventClient;

    @Scheduled(fixedRate = 60000)
    public void checkForScheduledMatches() {
        LocalDate today = LocalDate.now();
        List<MatchEntity> allMatches = matchRepository.findAll();

        for (MatchEntity match : allMatches) {
            if (match.getDate() != null && !match.getDate().isAfter(today) && match.getHomeScore() == null) {
                log.info("🚀 Scheduler starting Match ID: {}", match.getId());
                liveEventClient.startMatch(match.getId());
            }
        }
    }
}