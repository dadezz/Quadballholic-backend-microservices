package com.quadballholic.backend.authService.service;

import com.quadballholic.backend.userService.api.UserServiceAPI;
import com.quadballholic.backend.userService.entity.EntityUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceAPI userService;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<EntityUser> u = userService.findEntityUserByEmail(email);

        if (u.isPresent()) {
            return UserDetailsImpl.build(u.get());
        }else {
            return null;
        }

    }
}
