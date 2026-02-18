package com.quadballholic.backend.reservation.entity;

import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity extends AbstractAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // logical foreign keys
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long matchId;

    @Column
    private String seatNumber; // @TODO is it useful? Not included in the plan
}
