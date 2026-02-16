package com.quadballholic.backend.tournamentService.repository;

import com.quadballholic.backend.tournamentService.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
}
