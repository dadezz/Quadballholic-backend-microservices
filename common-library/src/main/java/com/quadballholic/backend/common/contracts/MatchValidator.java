package com.quadballholic.backend.common.contracts;

import java.time.LocalDate;

public interface MatchValidator {
    /**
     * Checks if a team exists by its ID.
     * @param matchId the unique identifier of the team
     * @return true if the team exists, false otherwise
     */
    boolean exists(Long matchId);
    LocalDate getMatchDataTime(Long matchId);
}
