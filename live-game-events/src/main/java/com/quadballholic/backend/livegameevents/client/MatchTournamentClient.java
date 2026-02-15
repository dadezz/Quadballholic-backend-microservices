package com.quadballholic.backend.livegameevents.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tournament-service", path = "/api/match-tournaments")
public interface MatchTournamentClient {

    @GetMapping("/{id}/next-match")
    Long getNextTournamentMatchIdById(@PathVariable("id") Long id);

}
