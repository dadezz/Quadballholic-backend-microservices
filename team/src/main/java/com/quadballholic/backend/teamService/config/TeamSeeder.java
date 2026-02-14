package com.quadballholic.backend.teamService.config;

import com.quadballholic.backend.teamService.service.TestTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamSeeder implements CommandLineRunner {

    private final TestTeamService testTeamService;
    @Override
    public void run(String... args) throws Exception {
        testTeamService.init();
    }

}
