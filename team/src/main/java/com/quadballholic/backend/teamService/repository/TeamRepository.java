package com.quadballholic.backend.teamService.repository;

import com.quadballholic.backend.teamService.entity.EntityTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<EntityTeam, Long> {

    Optional<EntityTeam> findByName(String name);

    boolean existsByName(String name);
}
