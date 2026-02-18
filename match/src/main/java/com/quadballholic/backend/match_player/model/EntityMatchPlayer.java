package com.quadballholic.backend.match_player.model;

import com.quadballholic.backend.match_player.enums.EnumPlayerPosition;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityMatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private Long playerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPlayerPosition playerPosition;

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private boolean isStarter;

    @Column(nullable = false)
    private boolean isOnTheField;

    @Column(nullable = false)
    private boolean receivedYellowCard = false;

    @Column(nullable = false)
    private boolean receivedRedCard = false;

    @Column(nullable = false)
    private boolean caughtSnitch = false;

    @Column(nullable = false)
    private int score = 0;


    public void scored(){
        score += 10;
    }

    public void caughtSnitch(){
        score += 30;
        caughtSnitch = true;
    }



}
