package com.quadballholic.backend.authService.repository;

import com.quadballholic.backend.authService.entity.EntityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<EntityToken, Long> {
    Optional <EntityToken> findEntityTokenByToken(String value);
}
