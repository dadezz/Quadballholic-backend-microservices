package com.quadballholic.backend.authService.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
