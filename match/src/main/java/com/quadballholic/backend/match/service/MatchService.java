package com.quadballholic.backend.match.service;

import com.quadballholic.backend.match.dto.MatchDto;
import com.quadballholic.backend.match.entity.MatchEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface MatchService {
    MatchEntity getMatchById(Long matchId);
    MatchEntity createMatch(MatchEntity match);
    MatchEntity updateMatch(MatchEntity match, Long id);
    void deleteMatchById(Long matchId);
    List<MatchEntity> findAllMatchesByIds(List<Long> matchIds);
    void updateScore(Long matchId, int homeTeamScore, int awayTeamScore);
    void setNextMatchTeamId(Long matchId, Long teamId);
    MatchEntity updateMatchSnitchCaught(Long id, Long catcherTeamId);
    void resetMatchSimulation(Long id);

    List<MatchDto> createMatches(@Valid List<MatchDto> matches);

    Boolean existsById(Long id);
    List<MatchEntity> getAllMatches();
}
