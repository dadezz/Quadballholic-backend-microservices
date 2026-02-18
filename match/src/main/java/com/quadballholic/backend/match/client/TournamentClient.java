package com.quadballholic.backend.match.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tournament-service", contextId = "tournamentClientForMatches")
public interface TournamentClient {

    @GetMapping("/api/tournaments/{id}")
    Boolean existsById(@PathVariable("id") Long tournamentId);
}
