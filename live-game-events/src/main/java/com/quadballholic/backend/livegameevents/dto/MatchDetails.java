package com.quadballholic.backend.livegameevents.dto;

public record MatchDetails(
        Long id,
        Long tournamentId,
        Long homeTeamId,
        Long awayTeamId
) {
}
