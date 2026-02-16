package com.quadballholic.backend.tournamentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StadiumDto {
    private Long id;
    private String name;
    private String address;
    private Integer capacity;
}
