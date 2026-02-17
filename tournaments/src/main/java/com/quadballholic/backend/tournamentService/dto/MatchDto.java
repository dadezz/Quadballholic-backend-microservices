package com.quadballholic.backend.tournamentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {
    private Long id;
    private Long tournamentId;
    private Long homeTeamId;
    private Long awayTeamId;
    private Long stadiumId;
    private LocalDate date;
    private String status;
    private Integer homeScore;
    private Integer awayScore;
    private Long snitchCaughtByTeamId;
}