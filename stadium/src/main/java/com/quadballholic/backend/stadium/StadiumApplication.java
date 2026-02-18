package com.quadballholic.backend.stadium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.quadballholic.backend"})
@EntityScan(basePackages = {"com.quadballholic.backend"})
@EnableJpaRepositories(basePackages = {"com.quadballholic.backend"})
public class StadiumApplication {

    public static void main(String[] args) {
        SpringApplication.run(StadiumApplication.class, args);
    }

}