package com.quadballholic.backend.match;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "com.quadballholic.backend", // Covers your current structure
        "com.quadballholic.backend.match",   // Covers the "old" structure (if client is there)
        "com.quadballholic.backend.match_player"   // Covers shared library
})
@ComponentScan("com.quadballholic.backend")
@EnableJpaRepositories("com.quadballholic.backend")
@EntityScan("com.quadballholic.backend")
public class MatchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchServiceApplication.class, args);
    }
}