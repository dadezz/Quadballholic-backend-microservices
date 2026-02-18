package com.quadballholic.backend.livegameevents.controller;

import com.quadballholic.backend.livegameevents.service.LiveGameEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class LiveSimulationController {

    private final LiveGameEventService liveGameEventService;

    // Mappiamo direttamente l'URL "classico" qui, anche se siamo in un altro modulo!
    @PostMapping("/matches/{id}/start-match")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<Void> startSimulation(@PathVariable("id") Long matchId) {
        liveGameEventService.startMatchSimulation(matchId);
        return ResponseEntity.ok().build();
    }
}
