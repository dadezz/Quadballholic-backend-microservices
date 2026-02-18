package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.client.MatchClient;
import com.quadballholic.backend.tournamentService.client.TeamClient;
import com.quadballholic.backend.tournamentService.client.UserClient;
import com.quadballholic.backend.tournamentService.dto.MatchDto;
import com.quadballholic.backend.tournamentService.entity.MatchTournamentEntity;
import com.quadballholic.backend.tournamentService.entity.TournamentEntity;
import com.quadballholic.backend.tournamentService.repository.MatchTournamentRepository;
import com.quadballholic.backend.tournamentService.repository.TeamTournamentRepository;
import com.quadballholic.backend.tournamentService.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService{

    private final TournamentRepository tournamentRepository;
    private final TeamTournamentRepository teamTournamentRepository;
    private final MatchTournamentRepository matchTournamentRepository;

    private final TeamClient teamClient;
    private final MatchClient matchClient;
    private final UserClient userClient;

    @Override
    public List<TournamentEntity> findAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    public TournamentEntity findTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found with id: " + id));
    }

    @Override
    public TournamentEntity createTournament(TournamentEntity tournament) {
        validateContent(tournament);
        tournament.setId(null);
        return tournamentRepository.save(tournament);
    }

    @Override
    public TournamentEntity updateTournament(Long id, TournamentEntity tournament) {
        if (tournament.getId() != null && !id.equals(tournament.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path does not match ID in body");
        }

        validateContent(tournament);

        TournamentEntity existingTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found for update"));

        existingTournament.setName(tournament.getName());
        existingTournament.setStartDate(tournament.getStartDate());
        existingTournament.setEndDate(tournament.getEndDate());
        existingTournament.setOrganizerId(tournament.getOrganizerId());

        return tournamentRepository.save(existingTournament);
    }

    @Override
    public void deleteTournamentById(Long id) {
        if (!tournamentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete: tournament not found");
        }

        teamTournamentRepository.deleteByTournamentId(id);
        matchTournamentRepository.deleteByTournamentId(id);
        tournamentRepository.deleteById(id);
    }

    @Override
    public void setStarted(Long id) {
        TournamentEntity tournament = tournamentRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found with id: " + id));
        tournament.setStarted(true);
        tournamentRepository.save(tournament);
    }

    @Override
    public boolean hasStarted(Long tournamentId) {
        TournamentEntity t = findTournamentById(tournamentId);
        return t.isStarted();
    }

    @Override
    public List<TournamentEntity> findAllTournamentsById(List<Long> id) {
        return tournamentRepository.findAllById(id);
    }

    @Override
    public void generateBracket(Long tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));

        List<Long> teamIds = teamTournamentRepository.findAllTeamsByTournament(tournamentId);
        if (teamIds.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough teams to generate a bracket");
        }

        Long defaultStadiumId = 1L;

        int numTeams = teamIds.size();
        int numRounds = (int) Math.ceil(Math.log(numTeams) / Math.log(2));

        Collections.shuffle(teamIds);

        List<MatchTournamentEntity> allBracketMatches = new ArrayList<>();
        int matchesInFirstRound = (int) Math.pow(2, numRounds - 1);

        for (int i = 0; i < matchesInFirstRound; i++) {
            MatchDto m = new MatchDto();
            m.setDate(tournament.getStartDate());
            m.setHomeScore(0);
            m.setAwayScore(0);
            m.setStadiumId(defaultStadiumId);
            m.setTournamentId(tournamentId);

            if (i * 2 < numTeams) m.setHomeTeamId(teamIds.get(i * 2));
            if (i * 2 + 1 < numTeams) m.setAwayTeamId(teamIds.get(i * 2 + 1));

            m = matchClient.createMatch(m);

            MatchTournamentEntity mt = new MatchTournamentEntity();
            mt.setTournamentId(tournamentId);
            mt.setMatchId(m.getId());
            mt.setRound(0);
            mt.setBracketIndex(i);
            allBracketMatches.add(mt);
        }

        for (int r = 1; r < numRounds; r++) {
            int matchesInRound = (int) Math.pow(2, numRounds - 1 - r);
            for (int i = 0; i < matchesInRound; i++) {
                MatchDto m = new MatchDto();
                m.setDate(tournament.getStartDate());
                m.setHomeScore(0);
                m.setAwayScore(0);
                m.setStadiumId(defaultStadiumId);
                m.setTournamentId(tournamentId);

                m = matchClient.createMatch(m);

                MatchTournamentEntity mt = new MatchTournamentEntity();
                mt.setTournamentId(tournamentId);
                mt.setMatchId(m.getId());
                mt.setRound(r);
                mt.setBracketIndex(i);
                allBracketMatches.add(mt);
            }
        }

        matchTournamentRepository.saveAll(allBracketMatches);
        setStarted(tournamentId);
    }

    private void validateContent(TournamentEntity tournamentEntity) {
        if (tournamentEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tournament entity is null");
        }
        if (tournamentEntity.getName() == null || tournamentEntity.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tournament name is empty");
        }
        if (tournamentEntity.getOrganizerId() == null || tournamentEntity.getOrganizerId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organizer id is empty");
        }
        if (tournamentEntity.getStartDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date is empty");
        }
        if (tournamentEntity.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date is empty");
        }
        if (tournamentEntity.getEndDate().isBefore(tournamentEntity.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date is before start date");
        }
        if (!userClient.existsById(tournamentEntity.getOrganizerId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organizer id not found");
        }
        if (!userClient.hasRole(tournamentEntity.getOrganizerId(), "ORGANIZATION_MANAGER")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Organizer not allowed");
        }
    }

}
