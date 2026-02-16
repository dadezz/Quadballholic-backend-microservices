package com.quadballholic.backend.tournamentService.controller;

import com.quadballholic.backend.tournamentService.entity.MatchTournamentEntity;
import com.quadballholic.backend.tournamentService.service.MatchTournamentService;
import com.quadballholic.backend.tournamentService.entity.TeamTournamentEntity;
import com.quadballholic.backend.tournamentService.service.TeamTournamentService;
import com.quadballholic.backend.tournamentService.entity.TournamentEntity;
import com.quadballholic.backend.tournamentService.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final TeamTournamentService teamTournamentService;
    private final MatchTournamentService matchTournamentService;

    @GetMapping("")
    public ResponseEntity<List<TournamentEntity>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.findAllTournaments());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<TournamentEntity> createTournament(@RequestBody TournamentEntity tournament) {
        return new ResponseEntity<>(tournamentService.createTournament(tournament), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentEntity> getTournamentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tournamentService.findTournamentById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<TournamentEntity> updateTournament(@RequestBody TournamentEntity tournament, @PathVariable("id") Long id) {
        TournamentEntity updated = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<TournamentEntity> deleteTournament(@PathVariable("id") Long id) {
        tournamentService.deleteTournamentById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tournamentId}/teams/{teamId}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<TeamTournamentEntity> addTeam(@PathVariable("tournamentId") Long tournamentId, @PathVariable("teamId") Long teamId) {
        return new ResponseEntity<>(teamTournamentService.createTeamTournament(tournamentId, teamId), HttpStatus.CREATED);
    }

    @GetMapping("/{tournamentId}/teams")
    public ResponseEntity<List<Long>> getTeamsTournamentId(@PathVariable("tournamentId") Long tournamentId) {
        return new ResponseEntity<>(teamTournamentService.findAllTeamsByTournament(tournamentId), HttpStatus.OK);
    }

    @DeleteMapping("/{tournamentId}/teams/{teamId}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<Long> deleteTeamFromTournament(@PathVariable("tournamentId") Long tournamentId,  @PathVariable("teamId") Long teamId) {
        teamTournamentService.deleteTeamFromTournament(tournamentId, teamId);
        return ResponseEntity.ok(teamId);
    }

    @GetMapping("/{tournamentId}/matches")
    public ResponseEntity<List<MatchTournamentEntity>> getMatchesTournamentId(@PathVariable("tournamentId") Long tournamentId) {
        return new ResponseEntity<>(matchTournamentService.findAllMatchesByTournament(tournamentId), HttpStatus.OK);
    }

    @DeleteMapping("/{tournamentId}/matches/{matchId}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<Long> deleteMatchFromTournament(@PathVariable("tournamentId") Long tournamentId,  @PathVariable("matchId") Long matchId) {
        matchTournamentService.deleteMatchFromTournament(tournamentId, matchId);
        return ResponseEntity.ok(matchId);
    }

    @PostMapping("/{id}/generate-bracket")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<Void> generateBracket(@PathVariable("id") Long id) {
        tournamentService.generateBracket(id);
        return ResponseEntity.ok().build();
    }

}