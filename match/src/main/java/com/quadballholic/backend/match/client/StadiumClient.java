package com.quadballholic.backend.match.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stadium-service")
public interface StadiumClient {
    @GetMapping("/api/stadiums/{id}")
    Boolean existsById(@PathVariable("id") Long stadiumId);
}

