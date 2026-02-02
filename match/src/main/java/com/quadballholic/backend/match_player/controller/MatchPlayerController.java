package com.quadballholic.backend.match_player.controller;

import com.quadballholic.backend.match_player.model.EntityMatchPlayer;
import com.quadballholic.backend.match_player.service.MatchPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
