package com.quadballholic.backend.match.controller;

import com.quadballholic.backend.match.client.LiveEventClient;
import com.quadballholic.backend.match_player.service.MatchPlayerService;
import com.quadballholic.backend.match.dto.SubmitRosterRequest;
import com.quadballholic.backend.match.entity.MatchEntity;
import com.quadballholic.backend.match.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final MatchPlayerService matchPlayerService;
    private final LiveEventClient liveEventClient;

    @GetMapping("/{id}")
    public ResponseEntity<MatchEntity> getMatchById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<MatchEntity> updateMatch(@PathVariable Long id, @RequestBody MatchEntity match) {
        return ResponseEntity.ok(matchService.updateMatch(match, id));
    }

    @PostMapping("/{id}/submit-roster")
    @PreAuthorize("hasRole('TEAM_MANAGER')") //TODO: check if it's the manager of the team in request body.
    public ResponseEntity<List<Long>> submitRoster(@PathVariable("id") Long id,@RequestBody @Valid SubmitRosterRequest rosterRequest) {
        List<Long> matchPlayerIds = matchPlayerService.setMatchRoster(id, rosterRequest.teamId(), rosterRequest.startingPlayerIds(), rosterRequest.benchPlayerIds());
        return new ResponseEntity<>(matchPlayerIds, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/start-match")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<List<Long>> startMatch(@PathVariable("id") Long id) {
        liveEventClient.startMatch(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/{id}/get-match-date")
    public ResponseEntity<LocalDate> getMatchDate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(matchService.getMatchById(id).getDate(), HttpStatus.OK);
    }

}
