package com.quadballholic.backend.tournamentService.client;

import com.quadballholic.backend.tournamentService.dto.MatchDto; // Usa il DTO vero!
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "match-service", contextId = "matchClientForTournament")
public interface MatchClient {

    @PostMapping("/api/matches/batch")
    void createMatches(@RequestBody List<MatchDto> matches);

     @PostMapping("/api/matches")
     MatchDto createMatch(@RequestBody MatchDto matchDto);

     @PostMapping("/api/matches/{id}/exists")
     boolean exists(@PathVariable Long id);

     @GetMapping("/api/matches/all")
     List<MatchDto> findAllMatchesByIds(List<Long> matchIds);

     @DeleteMapping("/api/matches/{id}")
     void deleteMatchById(@PathVariable Long id);
}