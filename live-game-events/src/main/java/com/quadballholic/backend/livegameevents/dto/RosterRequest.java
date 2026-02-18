package com.quadballholic.backend.livegameevents.dto;

import java.util.List;

public record RosterRequest(
        Long teamId,
        List<Long> startingIds,
        List<Long> benchIds
) {
}
