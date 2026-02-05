package com.quadballholic.backend.player.repository;

import com.quadballholic.backend.player.model.EntityPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<EntityPlayer, Long> {


    Optional<EntityPlayer> findByName(String name);

    List<EntityPlayer> findAllByTeamId(Long teamId);
}