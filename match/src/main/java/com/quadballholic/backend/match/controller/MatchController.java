package com.quadballholic.backend.match.controller;

import com.quadballholic.backend.match.client.LiveEventClient;
import com.quadballholic.backend.match.dto.MatchDto;
import com.quadballholic.backend.match.dto.MatchMapper;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final MatchPlayerService matchPlayerService;
    private final LiveEventClient liveEventClient;

    @PostMapping("/batch")
    public ResponseEntity<List<MatchDto>> createMatchesList(@Valid @RequestBody List<MatchDto> matches) {
        return ResponseEntity.ok(matchService.createMatches(matches));
    }

    @PostMapping("")
    public ResponseEntity<MatchDto> createMatch(@Valid @RequestBody MatchDto match) {
        return ResponseEntity.ok(MatchMapper.toDto(matchService.createMatch(MatchMapper.toEntity(match))));
    }

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
    @PreAuthorize("hasRole('TEAM_MANAGER')")
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

    @PatchMapping("/{id}/score")
    public ResponseEntity<Void> updateMatchScore(
            @PathVariable Long id,
            @RequestParam int homeScore,
            @RequestParam int awayScore) {

        matchService.updateScore(id, homeScore, awayScore);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/snitch-catch")
    public ResponseEntity<Void> updateMatchSnitchCaught(
            @PathVariable Long id,
            @RequestParam Long catcherTeamId) {

        matchService.updateMatchSnitchCaught(id, catcherTeamId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/set-team")
    public ResponseEntity<Void> updateMatchTeamId(
            @PathVariable Long id,
            @RequestParam Long teamId) {
        matchService.setNextMatchTeamId(id, teamId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reset-match")
    public ResponseEntity<Void> resetMatchSimulationById(
            @PathVariable Long id) {
        matchService.resetMatchSimulation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.existsById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MatchDto>> findAllMatchesByIds(List<Long> matchIds){
        List<MatchEntity> matches = matchService.findAllMatchesByIds(matchIds);
        List<MatchDto> dtos = new ArrayList<>();

        matches.forEach(match -> dtos.add(MatchMapper.toDto(match)));

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatchById(id);
        return ResponseEntity.noContent().build();
    }

}
