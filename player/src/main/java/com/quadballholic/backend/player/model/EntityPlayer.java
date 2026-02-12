package com.quadballholic.backend.player.model;

import com.quadballholic.backend.common.entity.Person;
import com.quadballholic.backend.player.enums.EnumPlayerPosition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "players")
// Hibernate overrides the standard DELETE with an UPDATE for soft delete
@SQLDelete(sql = "UPDATE players SET deleted = true, deleted_at = now() WHERE id = ?")
// Automatically filters out records where deleted is true
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityPlayer extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumPlayerPosition position;

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private Integer jerseyNumber;

    // Added field for soft delete logic
    @Column(nullable = false)
    private boolean deleted = false;

    // Added field to track when the record was soft-deleted
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public EntityPlayer(String name, EnumPlayerPosition position, Long teamId, Integer jerseyNumber) {
        this.name = name;
        this.position = position;
        this.teamId = teamId;
        this.jerseyNumber = jerseyNumber;
    }
}