package com.quadballholic.backend.teamService.security;

import com.quadballholic.backend.common.contracts.UserValidator;
import com.quadballholic.backend.teamService.entity.EntityTeam;
import com.quadballholic.backend.teamService.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component("teamSecurity")
@RequiredArgsConstructor
public class TeamSecurity {

    private final TeamRepository teamRepository;
    private final UserValidator userValidator;

    @Transactional(readOnly = true)
    public boolean isManagerOfTeam(Long teamId) {
        if (teamId == null) return false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return false;
        }

        String email = auth.getName();

        Optional<Long> userIdOpt = userValidator.getUserIdByEmail(email);

        if (userIdOpt.isEmpty()) return false;
        Long currentUserId = userIdOpt.get();

        Optional<EntityTeam> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isEmpty()) return false;

        EntityTeam team = teamOpt.get();

        return team.getManager() != null && team.getManager().equals(currentUserId);
    }
}