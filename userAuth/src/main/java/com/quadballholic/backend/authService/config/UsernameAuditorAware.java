package com.quadballholic.backend.authService.config;

import com.quadballholic.backend.authService.service.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UsernameAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = null;
        String username = "anonymousUser";
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(username);
        }
        try {
            principal = (UserDetailsImpl) authentication.getPrincipal();
            username = principal.getUsername();
        }catch (Exception ignored){}

        return Optional.of(username);
    }
}