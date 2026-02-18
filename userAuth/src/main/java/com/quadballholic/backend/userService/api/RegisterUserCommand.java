package com.quadballholic.backend.userService.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserCommand(
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String passwordHash
) {
}
