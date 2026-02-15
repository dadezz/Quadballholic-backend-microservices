package com.quadballholic.backend.livegameevents.controller;

import com.quadballholic.backend.livegameevents.dto.LiveGameEventDTO;
import com.quadballholic.backend.livegameevents.service.LiveGameEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LiveGameEventWebSocketController {

    private final LiveGameEventService liveGameEventService;

    @SubscribeMapping("/match/{matchId}/events")
    public List<LiveGameEventDTO> subscribeToMatchEvents(@DestinationVariable Long matchId) {
        // 1. User Connects -> Immediate "Direct Message" with full history
        return liveGameEventService.getAllEventsForMatch(matchId);
    }
}
