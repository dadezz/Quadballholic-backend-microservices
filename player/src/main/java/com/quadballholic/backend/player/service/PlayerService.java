package com.quadballholic.backend.player.service;

import com.quadballholic.backend.player.model.EntityPlayer;

import java.util.List;


public interface PlayerService {

    EntityPlayer savePlayer(EntityPlayer player);

    List<EntityPlayer> getAllPlayers();

    List<EntityPlayer> getPlayersByTeamId(Long teamId);

    EntityPlayer getPlayerById(Long id);

    EntityPlayer findEntityPlayerByName(String name);

    // Added to match the "missing endpoints" requirement
    EntityPlayer updatePlayer(Long id, EntityPlayer playerDetails);

    void deletePlayer(Long id);
}