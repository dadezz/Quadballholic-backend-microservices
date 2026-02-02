package com.quadballholic.backend.common.contracts;
import java.util.Optional;

public interface UserValidator {
    /**
     * Checks if a team exists by its ID.
     * @param userId the unique identifier of the team
     * @return true if the team exists, false otherwise
     */
    boolean existsUserWithRole(Long userId, String roleName);
    boolean existsUser(Long userId);
    Optional<Long> getUserIdByEmail(String email);
    boolean exists(Long userId);
    String getUserEmailById(Long userId);

    boolean hasRoleOrganizationManager(Long userId);
    boolean hasRoleTeamManager(Long userId);
    boolean hasRoleSpectator(Long userId);
}
