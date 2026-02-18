package com.quadballholic.backend.tournamentService.repository;

import com.quadballholic.backend.tournamentService.entity.MatchTournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchTournamentRepository extends JpaRepository<MatchTournamentEntity, Long> {
    boolean existsByTournamentIdAndMatchId(Long tournamentId, Long matchId);
    List<MatchTournamentEntity> findAllByTournamentId(Long tournamentId);
    void deleteByTournamentIdAndMatchId(Long tournamentId, Long matchId);
    Optional<MatchTournamentEntity> findByMatchId(Long matchId);
    Optional<MatchTournamentEntity> findByTournamentIdAndRoundAndBracketIndex(Long tournamentId, int round, int bracketIndex);
    void deleteByTournamentId(Long tournamentId);

}

