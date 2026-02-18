package com.quadballholic.backend.tournamentService.dto;

import java.time.LocalDate;
import java.util.List;

public record StartTournamentRequestDTO(
        List<Long> teamIds,
        LocalDate startDate,
        LocalDate endDate) {

}