package com.quadballholic.backend.match_player.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "player-service")
public interface PlayerClient {

    @GetMapping("/api/players/{id}/positions")
    String getPlayerPositionById(@PathVariable("id") Long playerId);

}
