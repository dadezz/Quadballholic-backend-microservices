package com.quadballholic.backend.userService.service;

import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.entity.EntityUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<EntityUser> getUserById(Long id);
    List<EntityUser> getAllUsers();
    EntityUser updateUser(Long id, EntityUser user);
    void deleteUser(Long id);

    void addRoleToUser(Long userId, EnumUserRoleName roleName);
    void removeRoleFromUser(Long userId, EnumUserRoleName roleName);

    Optional<EntityUser> findEntityUserByEmail(String email);
    Boolean userExists(String email);
}
