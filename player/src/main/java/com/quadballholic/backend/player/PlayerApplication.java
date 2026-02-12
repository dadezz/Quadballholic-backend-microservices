package com.quadballholic.backend.player;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.quadballholic.backend.player.client")
@ComponentScan(basePackages = {
        "com.quadballholic.backend.player",
        "com.quadballholic.backend.common"
})
@EnableJpaRepositories(basePackages = "com.quadballholic.backend.player.repository")
@EntityScan(basePackages = "com.quadballholic.backend.player.model")
public class PlayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayerApplication.class, args);
    }

}
