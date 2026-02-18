package com.quadballholic.backend.stadium.config;

import com.quadballholic.backend.stadium.service.TestStadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StadiumSeeder implements CommandLineRunner {

    private final TestStadiumService testStadiumService;
    @Override
    public void run(String... args) throws Exception {
        testStadiumService.init();
    }

}
