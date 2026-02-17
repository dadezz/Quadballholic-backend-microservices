package com.quadballholic.backend.tournamentService.repository;

import com.quadballholic.backend.tournamentService.entity.TeamTournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamTournamentRepository extends JpaRepository<TeamTournamentEntity, Long> {
    boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);
    List<TeamTournamentEntity> findAllByTournamentId(Long tournamentId);
    void deleteByTournamentIdAndTeamId(Long tournamentId, Long teamId);


    void deleteByTournamentId(Long tournamentId);

    @Query("SELECT t.teamId FROM TeamTournamentEntity t WHERE t.tournamentId = :tournamentId")
    List<Long> findAllTeamsByTournament(@Param("tournamentId") Long tournamentId);
}
