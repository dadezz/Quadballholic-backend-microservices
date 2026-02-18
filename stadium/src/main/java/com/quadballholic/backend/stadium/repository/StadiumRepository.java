package com.quadballholic.backend.stadium.repository;

import com.quadballholic.backend.stadium.model.EntityStadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<EntityStadium, Long> {
}