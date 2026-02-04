package com.quadballholic.backend.userService.service;

import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public void init(){

        EnumUserRoleName[] defaultRoleNames = EnumUserRoleName.values();

        for (EnumUserRoleName roleName : defaultRoleNames) {
            if (!userRoleRepository.existsEntityRoleByRoleName(roleName)) {
                EntityRole role = new EntityRole();
                role.setRoleName(roleName);
                userRoleRepository.save(role);
            }
        }

    }

    public EntityRole findByRoleName(EnumUserRoleName role) {

        return userRoleRepository.findEntityRoleByRoleName(role).orElse(null);
    }

    @Override
    public List<EntityRole> findAll() {
        return userRoleRepository.findAll();
    }

}
