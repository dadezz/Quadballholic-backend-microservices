package com.quadballholic.backend.livegameevents.model;

import com.quadballholic.backend.livegameevents.dto.LiveGameEventDTO;
import com.quadballholic.backend.livegameevents.enums.EnumLiveGameEventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "game_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityLiveGameEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id")
    private Long matchId; // Reference to the Match

    @Column(name = "team_id")
    private Long teamId; // Reference to the Match

    @Column(name = "player_id")
    private Long playerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumLiveGameEventType eventType;


    private int gameMinute;

    private int homeTeamScore;
    private int awayTeamScore;

    @CreationTimestamp
    private Instant createdTime;

    public EntityLiveGameEvent(Long matchId, Long teamId, Long playerId, EnumLiveGameEventType eventType, int gameMinute, int homeTeamScore, int awayTeamScore, Instant createdTime) {
        this.matchId = matchId;
        this.teamId = teamId;
        this.playerId = playerId;
        this.eventType = eventType;
        this.gameMinute = gameMinute;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.createdTime = createdTime;
    }

    public LiveGameEventDTO toDTO(Long homeTeamId, Long awayTeamId) {
        Map<Long, Integer> matchScore = new HashMap<>();
        matchScore.put(homeTeamId, homeTeamScore);
        matchScore.put(awayTeamId, awayTeamScore);
        return new LiveGameEventDTO(
                matchId,
                teamId,
                playerId,
                eventType,
                gameMinute,
                matchScore,
                createdTime
        );
    }

}
