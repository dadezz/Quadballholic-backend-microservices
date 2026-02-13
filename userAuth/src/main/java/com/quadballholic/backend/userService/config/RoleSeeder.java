package com.quadballholic.backend.userService.config;

import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class RoleSeeder implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        EnumUserRoleName[] defaultRoleNames = EnumUserRoleName.values();

        for (EnumUserRoleName roleName : defaultRoleNames) {
            if (!userRoleRepository.existsEntityRoleByRoleName(roleName)) {
                EntityRole role = new EntityRole();
                role.setRoleName(roleName);
                userRoleRepository.save(role);
            }
        }
    }
}
