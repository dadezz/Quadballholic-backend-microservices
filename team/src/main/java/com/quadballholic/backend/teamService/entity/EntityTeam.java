package com.quadballholic.backend.teamService.entity;

import com.quadballholic.backend.common.entity.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityTeam extends AbstractAuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String city;

    @Column(nullable = false)
    private String nation;

    @Column(name = "manager_id")
    private Long manager;

    public EntityTeam(String name, String city, String nation, Long manager/*, Long coach*/) {
        this.name = name;
        this.city = city;
        this.nation = nation;
        this.manager = manager;
        //this.coach = coach;
    }
}
