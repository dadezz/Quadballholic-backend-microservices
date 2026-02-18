package com.quadballholic.backend.match_player.controller;

import com.quadballholic.backend.match_player.dto.RosterRequest;
import com.quadballholic.backend.match_player.model.EntityMatchPlayer;
import com.quadballholic.backend.match_player.service.MatchPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/match-player")
@RequiredArgsConstructor
public class MatchPlayerController {

    private final MatchPlayerService matchPlayerService;

    @GetMapping("/{id}")
    public ResponseEntity<EntityMatchPlayer> getMatchPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(matchPlayerService.getMatchPlayerById(id));
    }

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<List<EntityMatchPlayer>> getMatchPlayersByMatchId(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchPlayerService.getMatchPlayersByMatchId(matchId));
    }

    @PatchMapping("/stats")
    public ResponseEntity<EntityMatchPlayer> updateStats(
            @RequestParam Long matchId,
            @RequestParam Long playerId,
            @RequestParam String eventType) {

        EntityMatchPlayer updatedPlayer = matchPlayerService.updatePlayerStats(matchId, playerId, eventType);
        return ResponseEntity.ok(updatedPlayer);
    }

    @PatchMapping("/set-match-roster/{matchId}")
    public ResponseEntity<Void> setMatchRoster(
            @PathVariable Long matchId,
            @RequestBody RosterRequest rosterRequest) {

        List<Long> matchPlayerIds = matchPlayerService.setMatchRoster(matchId, rosterRequest.teamId(), rosterRequest.startingIds(),rosterRequest.benchIds());
        return ResponseEntity.ok().build();
    }

}
