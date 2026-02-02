package com.quadballholic.backend.userService.repository;

import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.entity.EntityRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<EntityRole, Long> {

    boolean existsEntityRoleByRoleName(EnumUserRoleName role);

    Optional<EntityRole> findEntityRoleByRoleName(EnumUserRoleName role);
}
