package com.quadballholic.backend.authService.api;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank
        String token,

        @NotBlank
        String newPassword,

        @NotBlank
        String newPasswordConfirmation
) {
}
