package com.quadballholic.backend.livegameevents.service;

import com.quadballholic.backend.livegameevents.dto.LiveGameEventDTO;

import java.util.List;

public interface LiveGameEventService {

    void startMatchSimulation(Long matchId);
    void runGameLoop(Long matchId);

    List<LiveGameEventDTO> getAllEventsForMatch(Long matchId);
}
