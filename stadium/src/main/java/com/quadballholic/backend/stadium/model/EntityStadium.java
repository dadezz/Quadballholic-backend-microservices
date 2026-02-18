package com.quadballholic.backend.stadium.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "stadiums")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityStadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String address;

    @Column(name = "capacity")
    private Integer capacity;

    public EntityStadium(String name, String address, Integer capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }
}