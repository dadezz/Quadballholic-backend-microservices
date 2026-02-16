package com.quadballholic.backend.tournamentService.entity;

import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "match_tournament",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tournamentId", "matchId"}))
@Getter
@Setter
public class MatchTournamentEntity extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tournamentId;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private Integer round;

    @Column(name = "bracket_index")
    private Integer bracketIndex;

    @Column(name = "next_match_id")
    private Long nextMatchId;
}