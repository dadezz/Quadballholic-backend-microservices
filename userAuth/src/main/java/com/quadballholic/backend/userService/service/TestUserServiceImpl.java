package com.quadballholic.backend.userService.service;

import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.enums.EnumUserStatus;
import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.entity.EntityUser;
import com.quadballholic.backend.userService.repository.UserRepository;
import com.quadballholic.backend.userService.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TestUserServiceImpl implements TestUserService {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleService roleService;
    @Autowired
    private UserRoleRepository userRoleRepository;

    final PasswordEncoder passwordEncoder;

    @Override
    public void init() {

        Map<String, EnumUserRoleName> userTypeRolesMap = new HashMap<>();

        userTypeRolesMap.put("spectator",EnumUserRoleName.ROLE_SPECTATOR);
        userTypeRolesMap.put("teammanager",EnumUserRoleName.ROLE_TEAM_MANAGER);
        userTypeRolesMap.put("organization",EnumUserRoleName.ROLE_ORGANIZATION_MANAGER);

        for (String userType : userTypeRolesMap.keySet()) {
            EnumUserRoleName roleName  = userTypeRolesMap.get(userType);
            if(!userRepository.existsEntityUsersByEmail(roleName.toString() + "@test.com" )){
                EntityUser user = new EntityUser(
                        "test",
                        userType,
                        roleName + "@test.com",
                        passwordEncoder.encode("123456")
                );
                user.setStatus(EnumUserStatus.ACTIVE);
                Set<EntityRole> roles = new HashSet<>();
                Optional<EntityRole> r = userRoleRepository.findEntityRoleByRoleName(roleName);

                r.ifPresent(roles::add);

                user.setRole(roles);
                userRepository.save(user);
            }
        }

    }

    @Override
    public EntityUser getTestUserByRoleName(String roleName){
        Set<EntityRole> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(EnumUserRoleName.valueOf(roleName)));


        List<EntityUser> users = userRepository.findAll();
        for(EntityUser user : users){
            System.out.println((user.getEmail()));
        }
        String email = roleName + "@test.com";
        System.out.println(email);
        System.out.println(roles.isEmpty());
        Optional<EntityUser> u = userRepository.findEntityUsersByEmailAndRole(email, roles);

        return u.orElse(null);
    }
}
