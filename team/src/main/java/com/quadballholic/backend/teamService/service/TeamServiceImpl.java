package com.quadballholic.backend.teamService.service;

import com.quadballholic.backend.common.contracts.UserValidator;
import com.quadballholic.backend.teamService.entity.EntityTeam;
import com.quadballholic.backend.teamService.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserValidator userValidator;

    @Override
    public EntityTeam createTeam(EntityTeam team) {
        if (teamRepository.findByName(team.getName()).isPresent()) {
            throw new IllegalArgumentException("Team with this name already exists.");
        }

        if (team.getName() == null || team.getName().isBlank()) {
            throw new IllegalArgumentException("Team name is required");
        }

        validateUserRole(team.getManager(), "ROLE_TEAM_MANAGER");

        // Ensuring that this is a new insert
        team.setId(null);
        return teamRepository.save(team);
    }

    @Override
    public Optional<EntityTeam> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public List<EntityTeam> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public EntityTeam updateTeam(Long id, EntityTeam updatedTeam) {
        EntityTeam existing = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (updatedTeam.getName() != null && !updatedTeam.getName().isBlank()) {
            if (!existing.getName().equals(updatedTeam.getName()) && teamRepository.existsByName(updatedTeam.getName())) {
                throw new IllegalArgumentException("Name already taken");
            }
            existing.setName(updatedTeam.getName());
        }

        if (updatedTeam.getCity() != null) existing.setCity(updatedTeam.getCity());
        if (updatedTeam.getNation() != null) existing.setNation(updatedTeam.getNation());

        if (updatedTeam.getManager() != null) {
            validateUserRole(updatedTeam.getManager(), "ROLE_TEAM_MANAGER");
            existing.setManager(updatedTeam.getManager());
        }

        /*if (updatedTeam.getCoach() != null) {
            existing.setCoach(updatedTeam.getCoach());
        }*/

        return teamRepository.save(existing);
    }

    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete non-existent team");
        }
        teamRepository.deleteById(id);
    }

    @Override
    public List<EntityTeam> findAllTeamsById(List<Long> teamIds){
        return teamRepository.findAllById(teamIds);
    }

    /*@Override
    public EntityTeam assignCoach(Long teamId, Long coachId) {
        EntityTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        team.setCoach(coachId);
        return teamRepository.save(team);
    }*/

    // --- Private method for support ---
    private void validateUserRole(Long userId, String requiredRole) {
        if (userId == null) return;

        // call the contract (UserValidator)
        boolean isValid = userValidator.existsUserWithRole(userId, requiredRole);

        if (!isValid) {
            throw new IllegalArgumentException(
                    String.format("User with ID %d does not exist or does not have role %s", userId, requiredRole)
            );
        }
    }
}
