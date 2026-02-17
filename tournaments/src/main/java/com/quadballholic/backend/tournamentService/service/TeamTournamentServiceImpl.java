package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.client.TeamClient;
import com.quadballholic.backend.tournamentService.entity.TeamTournamentEntity;
import com.quadballholic.backend.tournamentService.repository.TeamTournamentRepository;
import com.quadballholic.backend.tournamentService.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamTournamentServiceImpl implements TeamTournamentService {

    private final TournamentRepository tournamentRepository;
    private final TeamTournamentRepository teamTournamentRepository;

    private final TeamClient teamClient;

    @Override
    public TeamTournamentEntity createTeamTournament(Long tournamentId, Long teamId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found");
        }
        if (!teamClient.exists(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        if (teamTournamentRepository.existsByTournamentIdAndTeamId(tournamentId, teamId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Team already registered in this tournament");
        }

        TeamTournamentEntity participation = new TeamTournamentEntity();
        participation.setTournamentId(tournamentId);
        participation.setTeamId(teamId);

        return teamTournamentRepository.save(participation);
    }

    @Override
    public List<Long> findAllTeamsByTournament(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found");
        }

        return teamTournamentRepository.findAllByTournamentId(tournamentId)
                .stream()
                .map(TeamTournamentEntity::getTeamId)
                .toList();
    }

    @Transactional
    @Override
    public void deleteTeamFromTournament(Long tournamentId, Long teamId) {
        if (!teamTournamentRepository.existsByTournamentIdAndTeamId(tournamentId, teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team is not registered in this tournament");
        }
        teamTournamentRepository.deleteByTournamentIdAndTeamId(tournamentId, teamId);
    }


}
