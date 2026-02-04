/**
 * TODO: Delete this controller immediately after auth module is implemented.
 * This controller is just for dev environment. Should never go to live version
 * */

package com.quadballholic.backend.userService.controller;

import com.quadballholic.backend.common.util.JwtUtils;
import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.entity.EntityUser;
import com.quadballholic.backend.userService.service.RoleService;
import com.quadballholic.backend.userService.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test-user")
public class TestUserController {

    @Autowired
    TestUserService testUserService;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private RoleService roleService;

    @PostMapping("")
    public ResponseEntity<?> createTestAccess(@RequestParam("role") String userRoleName) {
        System.out.println("Request for " +userRoleName + " arrived");

        for(EntityRole role : roleService.findAll()){
            System.out.println(role.getRoleName());
        }

        EntityUser user = testUserService.getTestUserByRoleName(userRoleName);
        System.out.println(user);
        if(user == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String accessTokenStr = jwtUtils.generateToken(user.getEmail());
        List<EnumUserRoleName> userRoles = user.getRole().stream()
                .map(EntityRole::getRoleName).toList();

        return ResponseEntity.ok(Map.of(
                "accessToken", accessTokenStr,
                "id", user.getId(),
                "email", user.getEmail(),
                "roles", userRoles
        ));
    }

    @GetMapping("/spectator")
    @PreAuthorize("hasRole('SPECTATOR')")
    public ResponseEntity<?> getTestAccessSpectator() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString(),
                "roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList().toString()
        ));
    }

    @GetMapping("/organization-manager")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<?> getTestAccessOrganization() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString(),
                "roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList().toString()
        ));
    }


    @GetMapping("/team-manager")
    @PreAuthorize("hasRole('TEAM_MANAGER')")
    public ResponseEntity<?> getTestAccessTeamManager() {



        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString(),
                "roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList().toString()
        ));
    }
}

