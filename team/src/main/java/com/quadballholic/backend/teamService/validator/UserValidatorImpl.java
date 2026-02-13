package com.quadballholic.backend.teamService.validator;

import com.quadballholic.backend.common.contracts.UserValidator;
import com.quadballholic.backend.teamService.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {

    private final UserClient userClient;

    @Override
    public boolean existsUserWithRole(Long userId, String roleName) {
        if (userId == null || roleName == null) return false;
        Boolean result = userClient.hasRole(userId, roleName);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public boolean existsUser(Long userId) {
        return exists(userId);
    }

    @Override
    public Optional<Long> getUserIdByEmail(String email) {
        try {
            Long id = userClient.getIdByEmail(email);
            return Optional.ofNullable(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean exists(Long userId) {
        if (userId == null) return false;
        Boolean result = userClient.existsById(userId);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public String getUserEmailById(Long userId) {
        return userClient.getEmailById(userId);
    }

    @Override
    public boolean hasRoleOrganizationManager(Long userId) {
        return existsUserWithRole(userId, "ROLE_ORGANIZATION_MANAGER");
    }

    @Override
    public boolean hasRoleTeamManager(Long userId) {
        return existsUserWithRole(userId, "ROLE_TEAM_MANAGER");
    }

    @Override
    public boolean hasRoleSpectator(Long userId) {
        return existsUserWithRole(userId, "ROLE_SPECTATOR");
    }

    @Override
    public void validateUserExists(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        Boolean exists = userClient.existsById(userId);

        if (Boolean.FALSE.equals(exists)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }
}