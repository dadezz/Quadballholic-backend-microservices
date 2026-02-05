package com.quadballholic.backend.player.controller;

import com.quadballholic.backend.player.model.EntityPlayer;
import com.quadballholic.backend.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<EntityPlayer> createPlayer(@RequestBody EntityPlayer player) {
        EntityPlayer savedPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EntityPlayer>> getAllPlayers() {
        List<EntityPlayer> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<EntityPlayer>> getPlayersByTeamId(@PathVariable("teamId") Long teamId) {
        List<EntityPlayer> players = playerService.getPlayersByTeamId(teamId);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<EntityPlayer> updatePlayer(@PathVariable("id") Long id, @RequestBody EntityPlayer playerDetails) {
        EntityPlayer updatedPlayer = playerService.updatePlayer(id, playerDetails);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<Void> deletePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityPlayer> getPlayerById(@PathVariable("id") Long id) {
        EntityPlayer player = playerService.getPlayerById(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    // Mapping for PlayerClient in MatchService
    @GetMapping("/{id}/position")
    public ResponseEntity<String> getPlayerPositionById(@PathVariable("id") Long id) {
        String position = playerService.getPlayerById(id).getPosition().toString();
        return ResponseEntity.ok(position);
    }
}