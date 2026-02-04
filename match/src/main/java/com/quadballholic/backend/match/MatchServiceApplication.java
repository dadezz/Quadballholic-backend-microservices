package com.quadballholic.backend.match;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "com.quadballholic.backend", // Covers your current structure
        "com.quadballholic.backend.match",   // Covers the "old" structure (if client is there)
        "com.quadballholic.backend.match_player"   // Covers shared library
})
// 👇 This tells Spring: "Look for entities in MY package AND the COMMON package"
@ComponentScan(basePackages = {
        "com.quadballholic.backend.match",
        "com.quadballholic.backend.match_player",
        "com.quadballholic.backend.common",

})
public class MatchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchServiceApplication.class, args);
    }
}