package com.quadballholic.backend.match.service;

import com.quadballholic.backend.match.client.StadiumClient;
import com.quadballholic.backend.match.client.TeamClient;
import com.quadballholic.backend.match.client.TournamentClient;
import com.quadballholic.backend.match.dto.MatchDto;
import com.quadballholic.backend.match.dto.MatchMapper;
import com.quadballholic.backend.match.entity.MatchEntity;
import com.quadballholic.backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    private final TournamentClient tournamentClient;
    private final TeamClient teamClient;
    private final StadiumClient stadiumClient;

    @Override
    public MatchEntity getMatchById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found with id: " + matchId));
    }

    public MatchEntity createMatch(MatchEntity matchEntity) {
        validateContent(matchEntity);
        matchEntity.setId(null);
        matchRepository.save(matchEntity);
        return matchEntity;
    }

    @Override
    public MatchEntity updateMatch(MatchEntity match, Long id) {
        if (match.getId() != null && !id.equals(match.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path does not match ID in body");
        }

        validateContent(match);
        MatchEntity existing = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found for update"));

        existing.setDate(match.getDate());
        existing.setAwayScore(match.getAwayScore());
        existing.setHomeScore(match.getHomeScore());
        existing.setAwayTeamId(match.getAwayTeamId());
        existing.setHomeTeamId(match.getHomeTeamId());
        existing.setStadiumId(match.getStadiumId());
        existing.setSnitchCaughtByTeamId(match.getSnitchCaughtByTeamId());

        return matchRepository.save(existing);
    }

    @Override
    public void deleteMatchById(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete: Match not found");
        }
        matchRepository.deleteById(matchId);
    }

    @Override
    public List<MatchEntity> findAllMatchesByIds(List<Long> matchIds){
        return matchRepository.findAllById(matchIds);
    }

    @Override
    public void updateScore(Long matchId, int homeTeamScore, int awayTeamScore) {
        Optional<MatchEntity> m = matchRepository.findById(matchId);
        if(m.isPresent()){
            MatchEntity match = m.get();
            match.setHomeScore(homeTeamScore);
            match.setAwayScore(awayTeamScore);
            matchRepository.save(match);
        }
    }

    @Override
    public void setNextMatchTeamId(Long matchId, Long teamId) {
        Optional<MatchEntity> m = matchRepository.findById(matchId);
        if(m.isPresent()){
            MatchEntity match = m.get();
            if(match.getHomeTeamId() == null){
                match.setHomeTeamId(teamId);
            }else{
                match.setAwayTeamId(teamId);
            }
            matchRepository.save(match);
        }
    }

    @Override
    public MatchEntity updateMatchSnitchCaught(Long id, Long catcherTeamId){

        MatchEntity match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setSnitchCaughtByTeamId(catcherTeamId);
        return matchRepository.save(match);
    }

    @Override
    public void resetMatchSimulation(Long id) {
        Optional<MatchEntity> m = matchRepository.findById(id);
        if(m.isPresent()){
            MatchEntity match = m.get();
            match.setAwayScore(0);
            match.setHomeScore(0);
            match.setSnitchCaughtByTeamId(null);
            matchRepository.save(match);
        }
    }

    @Override
    public List<MatchDto> createMatches(List<MatchDto> matches) {
        List<MatchDto> createdMatches = new ArrayList<>();
        for (MatchDto m : matches) {

            MatchEntity createdMatchEntity = createMatch(MatchMapper.toEntity(m));
            createdMatches.add(MatchMapper.toDto(createdMatchEntity));
        }
        return createdMatches;
    }

    @Override
    public Boolean existsById(Long id) {
        return matchRepository.existsById(id);
    }

    private void validateContent(MatchEntity match) {
        if (!tournamentClient.existsById(match.getTournamentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found");
        }
        if (!stadiumClient.existsById(match.getStadiumId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found");
        }
        if (match.getHomeTeamId() != null && !teamClient.existsById(match.getHomeTeamId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Home Team not found");
        }
        if (match.getAwayTeamId() != null && !teamClient.existsById(match.getAwayTeamId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Away Team not found");
        }

        if (match.getHomeTeamId() != null
                && match.getAwayTeamId() != null
                && match.getHomeTeamId().equals(match.getAwayTeamId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Home and Away teams cannot be the same");
        }

    }


}
