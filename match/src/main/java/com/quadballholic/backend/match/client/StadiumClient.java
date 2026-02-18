package com.quadballholic.backend.match.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stadium-service" , contextId = "stadiumClientForMatches")
public interface StadiumClient {
    @GetMapping("/api/stadium/{id}")
    Boolean existsById(@PathVariable("id") Long stadiumId);
}

