package com.quadballholic.backend.match.entity;

import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "matches")
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* EXTERNAL RELATIONS */

    @Column(nullable = false)
    private Long tournamentId;

    // they can be null because on elimination tournaments,
    // you don't know the winning teams
    @Column
    private Long homeTeamId;

    @Column
    private Long awayTeamId;

    @Column(nullable = false)
    private Long stadiumId;

    /* INTERNAL INFORMATION */
    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Integer homeScore;

    @Column
    private Integer awayScore;

    @Column // if nobody caught it -> null
    private Long snitchCaughtByTeamId;
}
