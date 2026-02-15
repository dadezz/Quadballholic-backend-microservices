package com.quadballholic.backend.livegameevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.quadballholic.backend"})
@ComponentScan("com.quadballholic.backend")
@EnableJpaRepositories("com.quadballholic.backend")
@EntityScan("com.quadballholic.backend")
public class LiveGameEventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveGameEventServiceApplication.class, args);
    }
}