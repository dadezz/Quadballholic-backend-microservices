package com.quadballholic.backend.match_officials.model;

import com.quadballholic.backend.common.entity.Person;
import com.quadballholic.backend.match_officials.enums.EnumRole; // Ensure this import matches your package
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "match_officials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityMatchOfficials extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnumRole role;


    public EntityMatchOfficials(String firstName, String lastName, EnumRole role) {
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
    }
}