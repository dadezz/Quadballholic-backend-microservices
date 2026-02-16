package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.entity.TeamTournamentEntity;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TeamTournamentService {
    TeamTournamentEntity createTeamTournament(Long tournamentId, Long teamId);

    List<Long> findAllTeamsByTournament(Long tournamentId);

    @Transactional
    void deleteTeamFromTournament(Long tournamentId, Long teamId);
}
