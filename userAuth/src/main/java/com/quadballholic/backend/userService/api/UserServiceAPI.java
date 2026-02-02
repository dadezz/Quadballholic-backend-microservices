package com.quadballholic.backend.userService.api;

import com.quadballholic.backend.userService.entity.EntityUser;

import java.util.Optional;

public interface UserServiceAPI {

    Long registerUser(RegisterUserCommand command);
    Optional<EntityUser> findEntityUserByEmail(String email);
    Boolean userExists(String email);
    void activateUser(Long userId);
    void resetUserPassword(Long userId, String newPassword);
}
