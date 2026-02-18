package com.quadballholic.backend.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(name = "match-service", path = "/api/matches")
public interface MatchClient {

    @GetMapping("/{id}")
    Boolean existsById(@PathVariable("id") Long id);

    @GetMapping("/{id}/get-match-date")
    LocalDate getMatchDate(@PathVariable("id") Long id);

}
