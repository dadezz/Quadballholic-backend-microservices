package com.quadballholic.backend.match_player.service;

import com.quadballholic.backend.match_player.client.PlayerClient;
import com.quadballholic.backend.match_player.enums.EnumPlayerPosition;
import com.quadballholic.backend.match_player.model.EntityMatchPlayer;
import com.quadballholic.backend.match_player.repository.MatchPlayerRepository;
import com.quadballholic.backend.match.entity.MatchEntity;
import com.quadballholic.backend.match.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchPlayerServiceImpl implements MatchPlayerService {

    private final MatchPlayerRepository matchPlayerRepository;
    private final MatchService matchService;
    private final PlayerClient playerClient;

    @Override
    public EntityMatchPlayer getMatchPlayerById(Long id){
        return matchPlayerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Player found")
        );
    }

    @Override
    public EntityMatchPlayer addPlayerToMatch(Long matchId, Long playerId, Long teamId, boolean isStarter) {
        if (matchPlayerRepository.existsByMatchIdAndPlayerId(matchId, playerId)) {
            throw new RuntimeException("Player is already in the roster for this match.");
        }

        EntityMatchPlayer player = EntityMatchPlayer.builder()
                .matchId(matchId)
                .playerId(playerId)
                .playerPosition(EnumPlayerPosition.valueOf(playerClient.getPlayerPositionById(playerId)))
                .teamId(teamId)
                .isStarter(isStarter)
                .isOnTheField(isStarter)
                .score(0)
                .receivedYellowCard(false)
                .receivedRedCard(false)
                .caughtSnitch(false)
                .build();

        return matchPlayerRepository.save(player);
    }

    @Override
    public List<EntityMatchPlayer> getMatchPlayersByMatchId(Long matchId) {
        return matchPlayerRepository.findByMatchId(matchId);
    }

    @Override
    public List<Long> setMatchRoster(Long matchId, Long teamId, List<Long> startingPlayerIds, List<Long> benchPlayerIds) {
        MatchEntity match = matchService.getMatchById(matchId);
        List<Long> matchPlayerIds = new ArrayList<>();
        for (Long startingPlayerId : startingPlayerIds) {
            EntityMatchPlayer matchPlayer = addPlayerToMatch(matchId, startingPlayerId, teamId, true);
            matchPlayerIds.add(matchPlayer.getId());
        }
        for (Long benchPlayerId : benchPlayerIds) {
            EntityMatchPlayer matchPlayer = addPlayerToMatch(matchId, benchPlayerId, teamId, false);
            matchPlayerIds.add(matchPlayer.getId());
        }
        return matchPlayerIds;
    }

    @Override
    @Transactional
    public EntityMatchPlayer updatePlayerStats(Long matchId, Long playerId, String eventType) {
        EntityMatchPlayer player = matchPlayerRepository.findByMatchIdAndPlayerId(matchId, playerId)
                .orElseThrow(() -> new RuntimeException("Player not found in this match roster"));

        switch (eventType) {
            case "SCORE":
                player.scored();
                break;
            case "YELLOW_CARD":
                if(player.isReceivedYellowCard()){
                    player.setReceivedYellowCard(false);
                    player.setReceivedRedCard(true);
                    break;
                }
                player.setReceivedYellowCard(true);
                break;
            case "RED_CARD":
                player.setReceivedRedCard(true);
                break;
            case "SNITCH_CATCH":
                player.setCaughtSnitch(true);
                break;
            case "SUBSTITUTION_IN":
                player.setOnTheField(true);
                break;
            case "SUBSTITUTION_OUT":
                player.setOnTheField(false);
                break;
            default:
                break;
        }

        return matchPlayerRepository.save(player);
    }
}
