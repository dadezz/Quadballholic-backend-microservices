package com.quadballholic.backend.match.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service", contextId = "teamClientForMatches")
public interface TeamClient {

    @GetMapping("/api/teams/{id}")
    Boolean existsById(@PathVariable("id") Long teamId);
}
