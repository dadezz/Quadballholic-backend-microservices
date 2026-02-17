package com.quadballholic.backend.tournamentService.controller;

import com.quadballholic.backend.tournamentService.dto.MatchDto;
import com.quadballholic.backend.tournamentService.dto.StartTournamentRequestDTO;
import com.quadballholic.backend.tournamentService.service.TournamentOrchestratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournament-management")
@RequiredArgsConstructor
public class TournamentManagementController {
    private final TournamentOrchestratorService to;

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZATION_MANAGER')")
    public ResponseEntity<List<List<MatchDto>>> startTournaments(@PathVariable("id") Long id,
                                                                 @RequestBody @Valid StartTournamentRequestDTO request) {
        return ResponseEntity.ok(
                to.startTournament(
                        request.teamIds(),
                        request.startDate(),
                        request.endDate(),
                        id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<List<MatchDto>>> getTournamentBracket(@PathVariable("id") Long id) {
        return ResponseEntity.ok(to.findAllMatchesByTournamentId(id));
    }
}
