package com.quadballholic.backend.common.contracts;

public interface TeamValidator {
    /**
     * Checks if a team exists by its ID.
     * @param teamId the unique identifier of the team
     * @return true if the team exists, false otherwise
     */
    boolean exists(Long teamId);
}
