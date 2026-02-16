package com.quadballholic.backend.match_officials.repository;

import com.quadballholic.backend.match_officials.model.EntityMatchOfficials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OfficialRepository extends JpaRepository<EntityMatchOfficials, Long> {
    Optional<EntityMatchOfficials> findEntityMatchOfficialsByFirstName(String name);
}
