package com.quadballholic.backend.match.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitRosterRequest(
        @NotNull
        Long teamId,

        @NotNull
        @Size(min = 7, max = 7)
        List <Long> startingPlayerIds,

        @NotNull
        List <Long> benchPlayerIds
) {
}
