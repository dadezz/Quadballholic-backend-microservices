package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.entity.MatchTournamentEntity;
import jakarta.transaction.Transactional;

import java.util.List;

public interface MatchTournamentService {
    MatchTournamentEntity createMatchTournament(Long tournamentId, Long matchId, Integer round, Integer bracketIndex, Long nextMatchId);
    List<MatchTournamentEntity> findAllMatchesByTournament(Long tournamentId);
    @Transactional
    void deleteMatchFromTournament(Long tournamentId, Long matchId);
    void updateNextMatch(Long currentMatchId, Long nextMatchId);
    Long getNextMatchTournamentId(Long matchId);
}
