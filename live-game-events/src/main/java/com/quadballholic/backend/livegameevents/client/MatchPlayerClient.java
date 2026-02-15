package com.quadballholic.backend.livegameevents.client;

import com.quadballholic.backend.livegameevents.dto.MatchPlayerDetails;
import com.quadballholic.backend.livegameevents.dto.RosterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "match-service", contextId = "matchPlayerClient", path = "/api/match-players")
public interface MatchPlayerClient {

    @GetMapping("/matches/{matchId}")
    List<MatchPlayerDetails> getMatchPlayersByMatchId(@PathVariable("matchId") Long matchId);

    // --- Reverse Update (New) ---
    @PatchMapping("/stats")
    MatchPlayerDetails updatePlayerStats(
        @RequestParam("matchId") Long matchId,
        @RequestParam("playerId") Long playerId,
        @RequestParam("eventType") String eventType
    );

    @PatchMapping("/set-match-roster/{matchId}")
    MatchPlayerDetails setMatchRoster(
        @PathVariable("matchId") Long matchId,
        @RequestBody RosterRequest rosterRequest
    );
}
