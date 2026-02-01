package com.quadballholic.backend.userService.service;


import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.entity.EntityRole;

import java.util.List;

public  interface RoleService {

    //Check and create roles if needed
    void init();

    EntityRole findByRoleName(EnumUserRoleName role);

    List<EntityRole> findAll();

}
