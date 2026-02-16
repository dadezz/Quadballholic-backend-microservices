package com.quadballholic.backend.livegameevents.client;

import com.quadballholic.backend.livegameevents.dto.MatchDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "match-service", contextId = "matchClient", path = "/api/matches")
public interface MatchClient {

    @GetMapping("/{id}")
    MatchDetails getById(@PathVariable("id") Long id);

    // --- Reverse Update (New) ---
    @PatchMapping("/{id}/score")
    void updateMatchScore(
            @PathVariable("id") Long id,
            @RequestParam("homeScore") int homeScore,
            @RequestParam("awayScore") int awayScore
    );

    @PatchMapping("{id}/snitch-catch")
    void updateMatchSnitchCaught(
            @PathVariable("id") Long id,
            @RequestParam("catcherTeamId") Long catcherTeamId
    );

    @PatchMapping("/{id}/set-team")
    void setNextMatchTeamId(
            @PathVariable("id") Long id,
            @RequestParam("teamId") Long teamId
    );

    @PatchMapping("/{id}/reset-match")
    void resetMatchSimulationById(
            @PathVariable("id") Long id
    );



}
