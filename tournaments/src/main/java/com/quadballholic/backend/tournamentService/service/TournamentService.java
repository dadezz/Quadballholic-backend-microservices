package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.entity.TournamentEntity;

import java.util.List;

public interface TournamentService {
    List<TournamentEntity> findAllTournaments();
    TournamentEntity findTournamentById(Long id);
    TournamentEntity createTournament(TournamentEntity tournamentEntity);
    TournamentEntity updateTournament(Long id, TournamentEntity tournamentEntity);
    void deleteTournamentById(Long id);

    void setStarted(Long id);

    boolean hasStarted(Long tournamentId);

    List<TournamentEntity> findAllTournamentsById(List<Long> id);

    void generateBracket(Long tournamentId);

}