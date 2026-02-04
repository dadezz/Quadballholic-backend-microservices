package com.quadballholic.backend.userService.service;

import com.quadballholic.backend.userService.entity.EntityUser;

public interface TestUserService {

    void init();

    EntityUser getTestUserByRoleName(String roleName);
}
