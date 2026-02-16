package com.quadballholic.backend.tournamentService.entity;

import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntity extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column (nullable = false)
    private Long organizerId;

    @Column (nullable = false)
    private boolean started;

    public TournamentEntity(String name, LocalDate startDate, LocalDate endDate, Long organizerId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organizerId = organizerId;
        this.started = false;
    }
}