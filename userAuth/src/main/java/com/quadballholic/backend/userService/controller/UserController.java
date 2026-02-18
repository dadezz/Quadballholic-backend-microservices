package com.quadballholic.backend.userService.controller;

import com.quadballholic.backend.userService.service.UserService;
import com.quadballholic.backend.userService.entity.EntityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id).isPresent());
    }

    @GetMapping("/{id}/has-role")
    public ResponseEntity<Boolean> hasRole(@PathVariable Long id, @RequestParam String roleName) {
        Optional<EntityUser> user = userService.getUserById(id);
        if (user.isEmpty()) return ResponseEntity.ok(false);

        boolean hasRole = user.get().getRole().stream()
                .anyMatch(r -> r.getRoleName().name().equals(roleName));
        return ResponseEntity.ok(hasRole);
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<String> getEmailById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user.getEmail()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<Long> getIdByEmail(@RequestParam String email) {
        return userService.findEntityUserByEmail(email)
                .map(user -> ResponseEntity.ok(user.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getUserName(@PathVariable("id") Long id) {
        String username = userService.getUserById(id)
                .map(EntityUser::getName)
                .orElse("Unknown");

        return ResponseEntity.ok(username);
    }
}