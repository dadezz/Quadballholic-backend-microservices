package com.quadballholic.backend.teamService.controller;

import com.quadballholic.backend.teamService.entity.EntityTeam;
import com.quadballholic.backend.teamService.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<EntityTeam> createTeam(@RequestBody EntityTeam team) {
        EntityTeam created = teamService.createTeam(team);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityTeam> getTeamById(@PathVariable("id") Long id) {
        return teamService.getTeamById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<List<EntityTeam>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER') or (hasRole('TEAM_MANAGER') and @teamSecurity.isManagerOfTeam(#id))")
    public ResponseEntity<EntityTeam> updateTeam(@PathVariable("id") Long id, @RequestBody EntityTeam team) {
        EntityTeam updated = teamService.updateTeam(id, team);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @PutMapping("/{id}/coach/{coachId}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER') or (hasRole('TEAM_MANAGER') and @teamSecurity.isManagerOfTeam(#id))")
    public ResponseEntity<EntityTeam> assignCoach(@PathVariable Long id, @PathVariable Long coachId) {
        EntityTeam updated = teamService.assignCoach(id, coachId);
        return ResponseEntity.ok(updated);
    }*/
}
