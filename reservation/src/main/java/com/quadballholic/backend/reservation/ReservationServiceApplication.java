package com.quadballholic.backend.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.quadballholic.backend")
@ComponentScan(basePackages = {
        "com.quadballholic.backend",
        "com.quadballholic.backend.common"})
@EntityScan(basePackages = {
        "com.quadballholic.backend",
        "com.quadballholic.backend.common"})
@EnableJpaRepositories(basePackages = {"com.quadballholic.backend"})
public class ReservationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}