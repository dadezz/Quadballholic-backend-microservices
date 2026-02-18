package com.quadballholic.backend.match.dto;

import com.quadballholic.backend.match.dto.MatchDto;
import com.quadballholic.backend.match.entity.MatchEntity;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    static public MatchDto toDto(MatchEntity entity) {
        if (entity == null) return null;

        return MatchDto.builder()
                .id(entity.getId())
                .tournamentId(entity.getTournamentId())
                .homeTeamId(entity.getHomeTeamId())
                .awayTeamId(entity.getAwayTeamId())
                .stadiumId(entity.getStadiumId())
                .date(entity.getDate())
                .homeScore(entity.getHomeScore())
                .awayScore(entity.getAwayScore())
                .snitchCaughtByTeamId(entity.getSnitchCaughtByTeamId())
                .build();
    }

    static public MatchEntity toEntity(MatchDto dto) {
        if (dto == null) return null;

        MatchEntity entity = new MatchEntity();
        entity.setId(dto.getId());
        entity.setTournamentId(dto.getTournamentId());
        entity.setHomeTeamId(dto.getHomeTeamId());
        entity.setAwayTeamId(dto.getAwayTeamId());
        entity.setStadiumId(dto.getStadiumId());
        entity.setDate(dto.getDate());
        entity.setHomeScore(dto.getHomeScore());
        entity.setAwayScore(dto.getAwayScore());
        entity.setSnitchCaughtByTeamId(dto.getSnitchCaughtByTeamId());

        return entity;
    }
}