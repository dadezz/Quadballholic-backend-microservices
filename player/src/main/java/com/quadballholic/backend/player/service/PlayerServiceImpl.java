package com.quadballholic.backend.player.service;

import com.quadballholic.backend.player.model.EntityPlayer;
import com.quadballholic.backend.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public EntityPlayer savePlayer(EntityPlayer player) {
        return playerRepository.save(player);
    }

    @Override
    public List<EntityPlayer> getAllPlayers() {
        System.out.println("DEBUG: Hit getAllPlayers");
        return playerRepository.findAll();
    }

    @Override
    public List<EntityPlayer> getPlayersByTeamId(Long teamId){
        return playerRepository.findAllByTeamId(teamId);
    }

    @Override
    public EntityPlayer getPlayerById(Long id) {
        System.out.println("DEBUG: Hit getPlayerById with ID: " + id); // <--- ADD THIS
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    @Override
    public EntityPlayer updatePlayer(Long id, EntityPlayer details) {
        EntityPlayer player = getPlayerById(id);
        player.setName(details.getName());
        player.setPosition(details.getPosition());
        player.setTeamId(details.getTeamId());
        player.setJerseyNumber(details.getJerseyNumber());
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Long id) {
        EntityPlayer player = getPlayerById(id);
        playerRepository.delete(player);
    }

    @Override
    public EntityPlayer findEntityPlayerByName(String name) {
        return playerRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found with name: " + name));
    }
}