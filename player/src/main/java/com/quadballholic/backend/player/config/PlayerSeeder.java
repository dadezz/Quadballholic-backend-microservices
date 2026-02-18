package com.quadballholic.backend.player.config;

import com.quadballholic.backend.player.service.TestPlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerSeeder {

    private final TestPlayerService testPlayerService;
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        CompletableFuture.runAsync(() -> {
            int maxRetries = 15;
            for (int i = 1; i <= maxRetries; i++) {
                try {
                    Thread.sleep(15000);
                    log.info("Seeding attempt {}/{}...", i, maxRetries);
                    testPlayerService.init();
                    log.info("✅ Seeding successful!");
                    return;
                } catch (Exception e) {
                    log.warn("Attempt {} failed: {}. Retrying in 10s...", i, e.getMessage());
                    try { Thread.sleep(10000); } catch (InterruptedException ignored) {}
                }
            }
            log.error(" FAILED TO SEED PLAYERS after {} attempts.", maxRetries);
        });
    }

}


