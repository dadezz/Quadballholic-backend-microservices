package com.quadballholic.backend.tournamentService.entity;

import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "team_tournament",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tournamentId", "teamId"}))
@Getter
@Setter
public class TeamTournamentEntity extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tournamentId;

    @Column(nullable = false)
    private Long teamId;

}