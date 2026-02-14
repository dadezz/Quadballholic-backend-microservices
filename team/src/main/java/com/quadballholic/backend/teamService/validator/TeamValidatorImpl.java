package com.quadballholic.backend.teamService.validator;

import com.quadballholic.backend.common.contracts.TeamValidator;
import com.quadballholic.backend.teamService.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamValidatorImpl implements TeamValidator {

    private final TeamService teamService;

    @Override
    public boolean exists(Long teamId) {
        return teamService.getTeamById(teamId).isPresent();
    }
}
