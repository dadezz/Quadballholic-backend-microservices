package com.quadballholic.backend.livegameevents.dto;

import com.quadballholic.backend.livegameevents.enums.EnumLiveGameEventType;


import java.time.Instant;
import java.util.Map;

public record LiveGameEventDTO(
        Long matchId,
        Long teamId,
        Long playerId,
        EnumLiveGameEventType type,       // GOAL, YELLOW_CARD, SNITCH_CATCH
        int gameMinute,
        Map<Long, Integer> matchScore,
        Instant createdTime
) {
}
