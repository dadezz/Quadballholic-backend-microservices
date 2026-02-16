package com.quadballholic.backend.match_player.dto;

import java.util.List;

public record RosterRequest(
        Long teamId,
        List<Long> startingIds,
        List<Long> benchIds
) {
}
