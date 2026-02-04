package com.quadballholic.backend.authService.service;

import com.quadballholic.backend.authService.enums.EnumTokenType;
import com.quadballholic.backend.authService.entity.EntityToken;
import com.quadballholic.backend.authService.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public EntityToken createToken(Long userId, EnumTokenType type, Long tokenExpirationMS) {
        EntityToken token = new EntityToken();
        token.setToken(UUID.randomUUID().toString());
        token.setTokenType(type);
        token.setUserId(userId);
        token.setExpiresAt(Instant.now().plusMillis(tokenExpirationMS));
        token = tokenRepository.save(token);
        return token;
    }

    public EntityToken validateToken(String tokenStr) {
        EntityToken token = tokenRepository
                .findEntityTokenByToken(tokenStr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid token"));

        if (token.isUsed() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.GONE,"Token expired or used");
        }
        return token;
    }

}
