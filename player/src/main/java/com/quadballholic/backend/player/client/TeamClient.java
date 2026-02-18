package com.quadballholic.backend.player.client;

import com.quadballholic.backend.player.dto.TeamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "team-service", contextId = "teamClientForPlayers")
public interface TeamClient {
        // We need an endpoint that gives us ALL teams so we can build the map
        @GetMapping("/api/teams")
        List<TeamDTO> getAllTeams();

}
