package com.quadballholic.backend.userService.config;

import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.entity.EntityUser;
import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.enums.EnumUserStatus;
import com.quadballholic.backend.userService.repository.UserRoleRepository;
import com.quadballholic.backend.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Order(2)
public class TestUserSeeder implements CommandLineRunner {

    private final UserRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Ensure Roles Exist (Critical for fresh DBs)
        if (roleRepository.count() == 0) {
            for (EnumUserRoleName roleName : EnumUserRoleName.values()) {
                EntityRole role = new EntityRole();
                role.setRoleName(roleName);
                roleRepository.save(role);
            }
        }

        // 2. Run your specific init logic
        init();
    }

    public void init() {
        Map<String, EnumUserRoleName> userTypeRolesMap = new HashMap<>();
        userTypeRolesMap.put("spectator", EnumUserRoleName.ROLE_SPECTATOR);
        userTypeRolesMap.put("teammanager", EnumUserRoleName.ROLE_TEAM_MANAGER);
        userTypeRolesMap.put("organization", EnumUserRoleName.ROLE_ORGANIZATION_MANAGER);

        for (String userType : userTypeRolesMap.keySet()) {
            EnumUserRoleName roleName = userTypeRolesMap.get(userType);
            String email = roleName.toString() + "@test.com"; // Matches "ROLE_SPECTATOR@test.com"

            if (userRepository.findByEmail(email).isEmpty()) {
                // Using Setters is safer than Constructor in case Entity definition changed
                EntityUser user = new EntityUser();
                user.setName("test");
                user.setSurname(userType); // "spectator"
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("123456")); // Matches your old password
                user.setStatus(EnumUserStatus.ACTIVE);

                // Fetch and Assign Role
                Set<EntityRole> roles = new HashSet<>(); // Using List to match standard JPA
                roleRepository.findEntityRoleByRoleName(roleName).ifPresent(roles::add);
                user.setRole(roles);

                userRepository.save(user);
                System.out.println("✅ Created Sync User: " + email + " | Pass: 123456");
            }
        }
    }

}