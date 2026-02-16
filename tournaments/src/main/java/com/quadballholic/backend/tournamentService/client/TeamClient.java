package com.quadballholic.backend.tournamentService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service", contextId = "teamClientForTournaments")
public interface TeamClient {
    @GetMapping("/api/teams/{id}/exists")
    boolean exists(@PathVariable("id") Long id);
}