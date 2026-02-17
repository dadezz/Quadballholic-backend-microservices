package com.quadballholic.backend.match_officials.config;

import com.quadballholic.backend.match_officials.service.TestOfficialService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchOfficialSeeder implements CommandLineRunner {

    private final TestOfficialService testOfficialService;
    @Override
    public void run(String... args) throws Exception {
        testOfficialService.init();
    }

}
