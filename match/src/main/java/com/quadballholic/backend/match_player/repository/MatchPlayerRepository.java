package com.quadballholic.backend.match_player.repository;

import com.quadballholic.backend.match_player.model.EntityMatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPlayerRepository  extends JpaRepository<EntityMatchPlayer,Long> {

    // Get everyone involved in the match
    List<EntityMatchPlayer> findByMatchId(Long matchId);

    // Get specific team's lineup
    List<EntityMatchPlayer> findByMatchIdAndTeamId(Long matchId, Long teamId);

    // Find a specific player record for a specific match (Critical for updating stats)
    Optional<EntityMatchPlayer> findByMatchIdAndPlayerId(Long matchId, Long playerId);

    // Prevent duplicates in roster
    boolean existsByMatchIdAndPlayerId(Long matchId, Long playerId);
}
