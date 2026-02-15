package com.quadballholic.backend.livegameevents.service;

import com.quadballholic.backend.livegameevents.dto.LiveGameEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveGameEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Publishes a game event to the specific match topic.
     * Clients subscribed to '/topic/match/{matchId}/events' will receive this.
     *
     * @param matchId The ID of the match happening now.
     * @param event   The DTO containing event details (Who, What, When).
     */
    public void publishEvent(Long matchId, LiveGameEventDTO event) {
        String destination = "/topic/match/" + matchId + "/events";

        messagingTemplate.convertAndSend("/topic/all-matches", event);

        log.debug("Broadcasting event to {}: {}", destination, event);

        messagingTemplate.convertAndSend(destination, event);
    }

}

