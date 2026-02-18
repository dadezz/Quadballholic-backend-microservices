package com.quadballholic.backend.match_player.service;

import com.quadballholic.backend.match_player.model.EntityMatchPlayer;

import java.util.List;

public interface MatchPlayerService {

    EntityMatchPlayer getMatchPlayerById(Long id);
    // Setup phase
    EntityMatchPlayer addPlayerToMatch(Long matchId, Long playerId, Long teamId, boolean isStarter);

    // View phase
    List<EntityMatchPlayer> getMatchPlayersByMatchId(Long matchId);

    List<Long> setMatchRoster(Long matchId, Long teamId, List<Long> startingPlayerIds, List<Long> benchPlayerIds);

    // Live Game phase (Updates stats based on events)
    EntityMatchPlayer updatePlayerStats(Long matchId, Long playerId, String eventType);

}
