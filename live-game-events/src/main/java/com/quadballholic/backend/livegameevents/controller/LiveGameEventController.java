package com.quadballholic.backend.livegameevents.controller;

import com.quadballholic.backend.livegameevents.dto.LiveGameEventDTO;
import com.quadballholic.backend.livegameevents.service.LiveGameEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/live-game-events")
@RequiredArgsConstructor
public class LiveGameEventController {

    private final LiveGameEventService liveGameEventService;

    @PostMapping("/match/{matchId}/start")
    public ResponseEntity<Void> startMatch(@PathVariable("matchId") Long matchId) {
        liveGameEventService.startMatchSimulation(matchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<LiveGameEventDTO>> getEventsByMatch(@PathVariable("matchId") Long matchId) {
        return ResponseEntity.ok(liveGameEventService.getAllEventsForMatch(matchId));
    }

}
