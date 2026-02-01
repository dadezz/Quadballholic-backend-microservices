package com.quadballholic.backend.userService.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient

// we have classes all over different modules/packages
@ComponentScan(basePackages = {"com.quadballholic.backend"})
@EntityScan(basePackages = {"com.quadballholic.backend"})
@EnableJpaRepositories(basePackages = {"com.quadballholic.backend"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}