package com.quadballholic.backend.userService.entity;

import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="roles")
public class EntityRole {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer ID;

    @Enumerated(EnumType.STRING)
    @Column
    private EnumUserRoleName roleName;


    public EntityRole(EnumUserRoleName roleName) {
        this.roleName = roleName;
    }

}
