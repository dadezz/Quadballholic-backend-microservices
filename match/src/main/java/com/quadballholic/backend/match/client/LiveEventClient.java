package com.quadballholic.backend.match.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "live-game-events-service", path = "/api/live-game-events")
public interface LiveEventClient {

    // The method name here doesn't matter, but the @PostMapping MUST match the other service
    @PostMapping("/match/{matchId}/start")
    void startMatch(@PathVariable("matchId") Long matchId);
}
