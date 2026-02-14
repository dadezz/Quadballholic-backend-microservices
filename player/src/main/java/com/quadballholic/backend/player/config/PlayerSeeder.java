package com.quadballholic.backend.player.config;

import com.quadballholic.backend.player.service.TestPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerSeeder {

    private final TestPlayerService testPlayerService;
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            // A brief pause ensures the background discovery task
            // has finished populating the local service cache.
            Thread.sleep(3000);
            testPlayerService.init();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}


