package com.quadballholic.backend.livegameevents.repository;

import com.quadballholic.backend.livegameevents.model.EntityLiveGameEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LiveGameEventRepository extends JpaRepository<EntityLiveGameEvent, Long> {
    List<EntityLiveGameEvent> findAllByMatchId(Long matchId);

    @Modifying
    @Transactional
    void deleteAllByMatchId(Long matchId);
}
