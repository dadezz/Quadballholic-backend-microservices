package com.quadballholic.backend.common.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public class Person {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;

}