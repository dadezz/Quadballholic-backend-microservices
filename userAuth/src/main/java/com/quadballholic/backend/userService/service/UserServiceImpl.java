package com.quadballholic.backend.userService.service;

import com.quadballholic.backend.userService.api.RegisterUserCommand;
import com.quadballholic.backend.userService.api.UserServiceAPI;
import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.enums.EnumUserStatus;
import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.entity.EntityUser;
import com.quadballholic.backend.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserServiceAPI {

    @Autowired
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public Optional<EntityUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<EntityUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public EntityUser updateUser(Long id, EntityUser user) {
        EntityUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        // mail and pwd are update in another wsy
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id); // (soft delete)
    }

    @Override
    public void addRoleToUser(Long userId, EnumUserRoleName roleName) {
        EntityUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        EntityRole role = roleService.findByRoleName(roleName);

        user.addUserRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeRoleFromUser(Long userId, EnumUserRoleName roleName) {
        EntityUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<EntityRole> roles = user.getRole(); // (roleS)
        roles.removeIf(r -> r.getRoleName().equals(roleName));

        user.setRole(roles);
        userRepository.save(user);
    }

    @Override
    public Optional<EntityUser> findEntityUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public Long registerUser(RegisterUserCommand command) {
        EntityUser user = new EntityUser(
                command.name(),
                command.surname(),
                command.email(),
                command.passwordHash()
        );

        EntityRole spectator = roleService.findByRoleName(EnumUserRoleName.ROLE_SPECTATOR);
        user.addUserRole(spectator);
        EntityUser saved = userRepository.save(user);
        return saved.getId();
    }

    @Override
    public Boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void activateUser(Long userId){
        EntityUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist"));
        user.setStatus(EnumUserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void resetUserPassword(Long userId, String newPasswordHash) {
        EntityUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist"));
        user.setPassword(newPasswordHash);
        userRepository.save(user);
    }
}
