package com.quadballholic.backend.authService.dto;

import com.quadballholic.backend.userService.enums.EnumUserRoleName;

import java.util.List;

public record SignInResponse(
        String accessToken,
        Integer expiresIn,
        UserInfo user
) {
    public record UserInfo(
            Long id,
            String email,
            String name,
            String surname,
            List<EnumUserRoleName> roles
    ) {}
}