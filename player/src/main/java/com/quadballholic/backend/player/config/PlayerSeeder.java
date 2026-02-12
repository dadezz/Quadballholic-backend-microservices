package com.quadballholic.backend.player.config;

import com.quadballholic.backend.player.service.TestPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerSeeder implements CommandLineRunner{

    private final TestPlayerService testPlayerService;
    @Override
    public void run(String... args) throws Exception {
        testPlayerService.init();
    }

}


