package com.quadballholic.backend.tournamentService.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.quadballholic.backend")
@ComponentScan(basePackages = {"com.quadballholic.backend"})
@EntityScan(basePackages = {"com.quadballholic.backend"})
@EnableJpaRepositories(basePackages = {"com.quadballholic.backend"})
public class TournamentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TournamentServiceApplication.class, args);
    }
}