package com.quadballholic.backend.livegameevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchPlayerDetails {
    Long id;
    Long playerId;
    Long matchId;
    Long teamId;
    String playerPosition;
    boolean isStarter;
    boolean isOnTheField;
    boolean receivedYellowCard;
    boolean receivedRedCard;
    boolean caughtSnitch;
    Integer score;


    public void caughtSnitch() {
        score += 30;
        caughtSnitch = true;
    }

    public void scored(){
        score += 10;
    }
}