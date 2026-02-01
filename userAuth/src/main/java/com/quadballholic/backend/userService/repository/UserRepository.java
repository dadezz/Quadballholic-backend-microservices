package com.quadballholic.backend.userService.repository;

import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.entity.EntityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<EntityUser, Long> {

    Optional<EntityUser> findByEmail(String email);

    Optional<EntityUser> findEntityUsersByEmailAndRole(String email, Set<EntityRole> roles);

    boolean existsEntityUsersByEmail(String mail);
}
