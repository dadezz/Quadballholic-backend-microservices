package com.quadballholic.backend.teamService.service;

import com.quadballholic.backend.teamService.entity.EntityTeam;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    EntityTeam createTeam(EntityTeam team);
    Optional<EntityTeam> getTeamById(Long id);
    List<EntityTeam> getAllTeams();
    EntityTeam updateTeam(Long id, EntityTeam updatedTeam);
    void deleteTeam(Long id);

    List<EntityTeam> findAllTeamsById(List<Long> teamIds);
    //EntityTeam assignCoach(Long teamId, Long coachId);
}

