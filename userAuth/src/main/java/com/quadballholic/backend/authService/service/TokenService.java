package com.quadballholic.backend.authService.service;

import com.quadballholic.backend.authService.enums.EnumTokenType;
import com.quadballholic.backend.authService.entity.EntityToken;

public interface TokenService {
    EntityToken createToken(Long userId, EnumTokenType type, Long tokenExpirationMS);
    EntityToken validateToken(String value, EnumTokenType type);
}
