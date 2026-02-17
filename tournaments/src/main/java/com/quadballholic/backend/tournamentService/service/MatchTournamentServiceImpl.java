package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.client.MatchClient;
import com.quadballholic.backend.tournamentService.entity.MatchTournamentEntity;
import com.quadballholic.backend.tournamentService.repository.MatchTournamentRepository;
import com.quadballholic.backend.tournamentService.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchTournamentServiceImpl implements MatchTournamentService {

    private final TournamentRepository tournamentRepository;
    private final MatchTournamentRepository matchTournamentRepository;

    private final MatchClient matchClient;

    @Override
    public MatchTournamentEntity createMatchTournament(Long tournamentId, Long matchId, Integer round, Integer bracketIndex, Long nextMatchId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found");
        }
        if (!matchClient.exists(matchId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found");
        }
        if (matchTournamentRepository.existsByTournamentIdAndMatchId(tournamentId, matchId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Match already registered in this tournament");
        }

        MatchTournamentEntity participation = new MatchTournamentEntity();
        participation.setTournamentId(tournamentId);
        participation.setMatchId(matchId);

        participation.setRound(round != null ? round : 0);
        participation.setBracketIndex(bracketIndex != null ? bracketIndex : 0);
        participation.setNextMatchId(nextMatchId);

        return matchTournamentRepository.save(participation);
    }

    @Override
    public List<MatchTournamentEntity> findAllMatchesByTournament(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found");
        }

        return matchTournamentRepository.findAllByTournamentId(tournamentId);
    }

    @Transactional
    @Override
    public void deleteMatchFromTournament(Long tournamentId, Long matchId) {
        if (!matchTournamentRepository.existsByTournamentIdAndMatchId(tournamentId, matchId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match is not registered in this tournament");
        }
        matchTournamentRepository.deleteByTournamentIdAndMatchId(tournamentId, matchId);
    }

    @Override
    public void updateNextMatch(Long currentMatchId, Long nextMatchId) {
        MatchTournamentEntity entity = matchTournamentRepository.findByMatchId(currentMatchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchTournament relation not found for match " + currentMatchId));

        entity.setNextMatchId(nextMatchId);
        matchTournamentRepository.save(entity);
    }

    @Override
    public Long getNextMatchTournamentId(Long matchId) {
        MatchTournamentEntity matchTournament = matchTournamentRepository.findByMatchId(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchTournament relation not found for match " + matchId));

        if(matchTournament.getNextMatchId() != null) {
            Long nextMatchId = matchTournament.getNextMatchId();
            MatchTournamentEntity nextMatchTournament = matchTournamentRepository.findByMatchId(nextMatchId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MatchTournament relation not found for match " + matchId));
            return nextMatchTournament.getMatchId();

        }
        return null;
    }
}
